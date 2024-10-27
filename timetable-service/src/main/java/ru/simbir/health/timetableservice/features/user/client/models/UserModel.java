package ru.simbir.health.timetableservice.features.user.client.models;

import ru.simbir.health.timetableservice.common.security.user.models.UserRole;
import ru.simbir.health.timetableservice.common.security.user.models.UserSessionDetails;

import java.util.Collection;

public record UserModel(
        Long userId,
        Collection<UserRole> roles,
        Boolean active
) implements UserSessionDetails {

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public Collection<UserRole> getRoles() {
        return roles;
    }

    @Override
    public Boolean isActive() {
        return active;
    }
}
