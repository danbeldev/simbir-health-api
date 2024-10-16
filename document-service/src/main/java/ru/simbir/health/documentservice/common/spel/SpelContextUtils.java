package ru.simbir.health.documentservice.common.spel;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

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

        Map<String, Object> request = new HashMap<>();
        String[] paramNames = methodSignature.getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            request.put(paramName, args[i]);
        }
        context.setVariable("request", request);
        return context;
    }

    public static StandardEvaluationContext createRestContext(Object result, JoinPoint joinPoint) {
        var context = new StandardEvaluationContext();

        createResponseContext(result, context);
        createRequestContext(joinPoint, context);

        return context;
    }
}