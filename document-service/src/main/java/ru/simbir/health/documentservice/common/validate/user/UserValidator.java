package ru.simbir.health.documentservice.common.validate.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;
import ru.simbir.health.documentservice.features.user.client.UserServiceClient;

@Component
@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<ValidUser, Long> {

    private final UserServiceClient userServiceClient;

    private UserRole[] allowedRoles;
    private boolean requireAll;

    @Override
    public void initialize(ValidUser constraintAnnotation) {
        allowedRoles = constraintAnnotation.roles();
        requireAll = constraintAnnotation.requireAll();
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        return userServiceClient.exists(userId, allowedRoles, requireAll);
    }
}