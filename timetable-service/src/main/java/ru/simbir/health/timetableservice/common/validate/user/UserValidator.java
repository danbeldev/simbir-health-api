package ru.simbir.health.timetableservice.common.validate.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.simbir.health.timetableservice.common.security.user.models.UserRole;
import ru.simbir.health.timetableservice.features.user.client.UserServiceClient;

import java.util.Arrays;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<ValidUser, Long> {

    private final UserServiceClient userServiceClient;

    private Collection<UserRole> allowedRoles;
    private boolean requireAll;

    @Override
    public void initialize(ValidUser constraintAnnotation) {
        allowedRoles = Arrays.stream(constraintAnnotation.roles()).toList();
        requireAll = constraintAnnotation.requireAll();
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        return userServiceClient.exists(userId, allowedRoles, requireAll);
    }
}