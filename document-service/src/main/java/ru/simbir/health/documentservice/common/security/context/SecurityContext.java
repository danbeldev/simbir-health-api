package ru.simbir.health.documentservice.common.security.context;

import lombok.Getter;
import lombok.Setter;
import ru.simbir.health.documentservice.common.security.user.models.UserSessionDetails;

@Getter
@Setter
public class SecurityContext {
    private UserSessionDetails userSession;
    private String accessToken;
}
