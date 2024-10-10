package ru.simbir.health.accountservice.features.security.dto;

import lombok.Data;

@Data
public class JwtResponseDto {
    private final Long userId;
    private final String accessToken;
    private final String refreshToken;
}
