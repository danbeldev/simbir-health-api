package ru.simbir.health.documentservice.common.validate.comparison;

import ru.simbir.health.documentservice.common.security.user.models.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldComparison {

    String firstField();

    String secondField();

    String condition() default "";

    UserRole[] excludedRoles() default {};
}