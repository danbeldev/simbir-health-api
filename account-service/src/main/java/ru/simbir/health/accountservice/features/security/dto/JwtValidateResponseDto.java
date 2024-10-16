package ru.simbir.health.accountservice.features.security.dto;

import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

import java.util.Collection;

public record JwtValidateResponseDto(
        Long userId,
        Collection<UserRoleEntityId.Role> roles
) {}
