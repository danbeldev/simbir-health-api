package ru.simbir.health.hospitalservice.features.user.client.models;

import ru.simbir.health.hospitalservice.common.security.user.models.UserRole;
import ru.simbir.health.hospitalservice.common.security.user.models.UserSessionDetails;

import java.util.Collection;

public record UserModel(
        Long userId,
        Collection<UserRole> roles
) implements UserSessionDetails {

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public Collection<UserRole> getRoles() {
        return roles;
    }
}
