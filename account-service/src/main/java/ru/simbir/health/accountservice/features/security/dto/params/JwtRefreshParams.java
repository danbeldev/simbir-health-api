package ru.simbir.health.accountservice.features.security.dto.params;

import jakarta.validation.constraints.NotBlank;

public record JwtRefreshParams(
        @NotBlank(message = "{jwt.refreshtoken.notblank}")
        String refreshToken
) {}
