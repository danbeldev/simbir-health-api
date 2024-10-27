package ru.simbir.health.hospitalservice.common.security.user.models;

import java.util.Collection;

public interface UserSessionDetails {

    Long getId();

    Collection<UserRole> getRoles();

    default Boolean isActive() { return true; }
}
