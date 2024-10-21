package ru.simbir.health.accountservice.features.user.dto.params;

import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

import java.util.List;

public record AdminCreateOrUpdateUserParams(
        String lastName,
        String firstName,
        String username,
        String password,
        List<UserRoleEntityId.Role> roles
) {}