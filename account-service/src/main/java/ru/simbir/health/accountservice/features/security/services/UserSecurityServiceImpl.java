package ru.simbir.health.accountservice.features.security.services;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.accountservice.common.message.LocalizedMessageService;
import ru.simbir.health.accountservice.features.security.dto.JwtResponseDto;
import ru.simbir.health.accountservice.features.security.dto.JwtValidateResponseDto;
import ru.simbir.health.accountservice.features.security.dto.params.JwtRefreshParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignInParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignUpParams;
import ru.simbir.health.accountservice.features.security.jwt.JwtTokenProvider;
import ru.simbir.health.accountservice.features.security.jwt.JwtUserDetails;
import ru.simbir.health.accountservice.features.security.services.activeToken.ActiveTokenService;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;
import ru.simbir.health.accountservice.features.user.services.UserService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ActiveTokenService activeTokenService;
    private final LocalizedMessageService localizedMessageService;

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

        var roles = List.of(UserRoleEntityId.Role.User);
        userService.updateRoles(savedUser, roles);

        return generationJwtResponse(savedUser, roles);
    }

    @Override
    @Transactional
    public JwtResponseDto signIn(SignInParams params) {
        UserEntity user = userService.getByUsername(params.username());

        if (!passwordEncoder.matches(params.password(), user.getPassword())) {
            var message = localizedMessageService.getMessage("user.invalid.password");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
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
        } catch (JwtException ex) {
            return new JwtValidateResponseDto(false);
        }
    }

    @Override
    @Transactional
    public JwtResponseDto refreshToken(JwtRefreshParams params) {
        if (!jwtTokenProvider.validateRefreshToken(params.refreshToken())) {
            var message = localizedMessageService.getMessage("token.invalid.refresh");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        UUID tokenId = jwtTokenProvider.getTokenIdWithRefreshToken(params.refreshToken());
        activeTokenService.deleteById(tokenId);

        String username = jwtTokenProvider.getUsernameWithRefreshToken(params.refreshToken());
        UserEntity user = userService.getByUsername(username);

        return generationJwtResponse(user);
    }

    private JwtResponseDto generationJwtResponse(UserEntity user) {
        UUID tokenId = activeTokenService.create();
        return generationJwtResponse(user, tokenId);
    }

    private JwtResponseDto generationJwtResponse(UserEntity user, Collection<UserRoleEntityId.Role> roles) {
        UUID tokenId = activeTokenService.create();
        return generationJwtResponse(user, tokenId, roles);
    }

    private JwtResponseDto generationJwtResponse(UserEntity user, UUID tokenId) {
        return generationJwtResponse(user, tokenId, user.getRoles().stream().map(r -> r.getId().getRole()).toList());
    }

    private JwtResponseDto generationJwtResponse(UserEntity user, UUID tokenId, Collection<UserRoleEntityId.Role> roles) {
        String accessToken = jwtTokenProvider.generateAccessToken(tokenId, user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(tokenId, user);

        return new JwtResponseDto(
                user.getId(),
                accessToken,
                refreshToken,
                roles
        );
    }
}
