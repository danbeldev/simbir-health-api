package ru.simbir.health.timetableservice.common.spel;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelContextUtils {

    public static StandardEvaluationContext createResponseContext(Object result) {
        var context = new StandardEvaluationContext();
        return createResponseContext(result, context);
    }

    public static StandardEvaluationContext createResponseContext(Object result, StandardEvaluationContext context) {
        context.setVariable("response", result);
        return context;
    }

    public static StandardEvaluationContext createRequestContext(JoinPoint joinPoint) {
        var context = new StandardEvaluationContext();
        return createRequestContext(joinPoint, context);
    }


    public static StandardEvaluationContext createRequestContext(JoinPoint joinPoint, StandardEvaluationContext context) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        var parameterNames = methodSignature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return context;
    }

    public static StandardEvaluationContext createRestContext(Object result, JoinPoint joinPoint) {
        var context = new StandardEvaluationContext();

        createResponseContext(result, context);
        createRequestContext(joinPoint, context);

        return context;
    }
}