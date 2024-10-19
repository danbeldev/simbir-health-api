package ru.simbir.health.timetableservice.common.security.user.models;

import java.util.Collection;

public interface UserSessionDetails {

    Long getId();

    Collection<UserRole> getRoles();
}
