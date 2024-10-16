package ru.simbir.health.documentservice.common.security.authenticate;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ru.simbir.health.documentservice.common.security.authenticate.models.LogicalOperation;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = "bearerAuth")
public @interface Authenticate {
    UserRole[] roles() default {};

    String parameterUserId() default "";

    LogicalOperation operation() default LogicalOperation.OR;
}
