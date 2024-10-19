package ru.simbir.health.timetableservice.common.security.context;

import lombok.Getter;
import lombok.Setter;
import ru.simbir.health.timetableservice.common.security.user.models.UserSessionDetails;

@Getter
@Setter
public class SecurityContext {
    private UserSessionDetails userSession;
}
