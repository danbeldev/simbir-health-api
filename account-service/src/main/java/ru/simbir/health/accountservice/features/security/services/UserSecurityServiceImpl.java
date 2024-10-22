package ru.simbir.health.accountservice.features.security.services;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.accountservice.features.security.dto.JwtResponseDto;
import ru.simbir.health.accountservice.features.security.dto.JwtValidateResponseDto;
import ru.simbir.health.accountservice.features.security.dto.params.JwtRefreshParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignInParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignUpParams;
import ru.simbir.health.accountservice.features.security.jwt.JwtTokenProvider;
import ru.simbir.health.accountservice.features.security.jwt.JwtUserDetails;
import ru.simbir.health.accountservice.features.security.services.activeToken.ActiveTokenService;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;
import ru.simbir.health.accountservice.features.user.services.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ActiveTokenService activeTokenService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public JwtResponseDto signUp(SignUpParams params) {
        UserEntity user = new UserEntity();

        user.setLastName(params.lastName());
        user.setFirstName(params.firstName());
        user.setUsername(params.username());
        user.setPassword(params.password());

        UserEntity savedUser = userService.create(user);

        return generationJwtResponse(savedUser);
    }

    @Override
    @Transactional
    public JwtResponseDto signIn(SignInParams params) {
        UserEntity user = userService.getByUsername(params.username());

        if (!passwordEncoder.matches(params.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }

        return generationJwtResponse(user);
    }

    @Override
    @Transactional
    public void signOut(UUID tokenId) {
        activeTokenService.deleteById(tokenId);
    }

    @Override
    @Transactional(readOnly = true)
    public JwtValidateResponseDto validateToken(String accessToken) {
        try {
            var authorization = (JwtUserDetails) jwtTokenProvider.getUserDetailsByAccessToken(accessToken);
            return new JwtValidateResponseDto(authorization.getUserId(), authorization.getAuthorities());
        }catch (JwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JwtResponseDto refreshToken(JwtRefreshParams params) {
        if (!jwtTokenProvider.validateRefreshToken(params.refreshToken()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token");

        String username = jwtTokenProvider.getUsernameWithRefreshToken(params.refreshToken());
        UserEntity user = userService.getByUsername(username);
        UUID tokenId = jwtTokenProvider.getTokenIdWithRefreshToken(params.refreshToken());

        return generationJwtResponse(user, tokenId);
    }

    private JwtResponseDto generationJwtResponse(UserEntity user) {
        UUID tokenId = activeTokenService.create();
        return generationJwtResponse(user, tokenId);
    }

    private JwtResponseDto generationJwtResponse(UserEntity user, UUID tokenId) {
        String accessToken = jwtTokenProvider.generateAccessToken(tokenId, user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(tokenId, user);

        return new JwtResponseDto(
                user.getId(),
                accessToken,
                refreshToken
        );
    }
}
