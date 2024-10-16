package ru.simbir.health.documentservice.common.security.user.models;

import java.util.Collection;

public interface UserSessionDetails {

    Long getId();

    Collection<UserRole> getRoles();
}
