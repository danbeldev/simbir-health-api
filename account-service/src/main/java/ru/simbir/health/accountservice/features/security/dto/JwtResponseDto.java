package ru.simbir.health.accountservice.features.security.dto;

import lombok.Data;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

import java.util.Collection;

@Data
public class JwtResponseDto {
    private final Long userId;
    private final String accessToken;
    private final String refreshToken;
    private final Collection<UserRoleEntityId.Role> roles;
}
