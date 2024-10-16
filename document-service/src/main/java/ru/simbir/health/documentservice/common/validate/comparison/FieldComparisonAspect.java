package ru.simbir.health.documentservice.common.validate.comparison;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import ru.simbir.health.documentservice.common.security.context.SecurityContextHolder;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;

import static ru.simbir.health.documentservice.common.spel.SpelContextUtils.createRestContext;

@Aspect
@Component
public class FieldComparisonAspect {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(fieldComparison)")
    public Object validateFieldMatch(ProceedingJoinPoint joinPoint, FieldComparison fieldComparison) throws Throwable {
        Object result = joinPoint.proceed();
        var context = createRestContext(result, joinPoint);

        var condition = fieldComparison.condition();
        if (!condition.isEmpty()) {
            var shouldCompare = parser.parseExpression(condition).getValue(context, Boolean.class);
            if (shouldCompare == null || !shouldCompare) {
                return result;
            }
        }

        var roles = fieldComparison.excludedRoles();
        if (roles.length > 0) {
            var userSessionRoles = SecurityContextHolder.getContext().getUserSession().getRoles();
            for (UserRole role : roles) {
                if (userSessionRoles.contains(role)) return result;
            }
        }

        var firstField = fieldComparison.firstField();
        var secondField = fieldComparison.secondField();

        var requestValue = parser.parseExpression(firstField).getValue(context);
        var responseValue = parser.parseExpression(secondField).getValue(context);

        if (requestValue != null && !requestValue.equals(responseValue)) {
            throw new IllegalArgumentException("Request parameter does not match the value in the response.");
        }

        return result;
    }
}