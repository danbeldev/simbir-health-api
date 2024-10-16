package ru.simbir.health.accountservice.features.security.services;

import ru.simbir.health.accountservice.features.security.dto.JwtResponseDto;
import ru.simbir.health.accountservice.features.security.dto.JwtValidateResponseDto;
import ru.simbir.health.accountservice.features.security.dto.params.JwtRefreshParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignInParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignUpParams;

import java.util.UUID;

public interface UserSecurityService {

    JwtResponseDto signUp(SignUpParams params);

    JwtResponseDto signIn(SignInParams params);

    void signOut(UUID tokenId);

    JwtValidateResponseDto validateToken(String accessToken);

    JwtResponseDto refreshToken(JwtRefreshParams params);
}
