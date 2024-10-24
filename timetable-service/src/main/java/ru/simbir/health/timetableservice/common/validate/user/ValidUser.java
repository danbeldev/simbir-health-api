package ru.simbir.health.timetableservice.common.validate.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.simbir.health.timetableservice.common.security.user.models.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUser {
    String message() default "{error.user.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    UserRole[] roles() default {};

    boolean requireAll() default false;
}