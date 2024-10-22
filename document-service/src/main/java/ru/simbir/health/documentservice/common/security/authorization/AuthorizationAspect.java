package ru.simbir.health.documentservice.common.security.authorization;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final ApplicationContext applicationContext;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(authorization)")
    public Object handleAuthorization(ProceedingJoinPoint joinPoint, Authorization authorization) throws Throwable {
        var proceedResult = !authorization.executeBefore() ? joinPoint.proceed() : null;

        var signature = (MethodSignature) joinPoint.getSignature();
        var args = joinPoint.getArgs();

        var context = new StandardEvaluationContext();
        context.setVariable("result", proceedResult);
        var parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        var result = invokeMethodWithSpEL(authorization.value(), context);

        if (!(boolean) result) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return authorization.executeBefore() ? joinPoint.proceed() : proceedResult;
    }

    private Object invokeMethodWithSpEL(String methodCall, StandardEvaluationContext context) throws Exception {
        // Парсим строку на бин и метод
        String beanMethodExpression = methodCall.replaceAll("\\(.*\\)", "");
        String[] parts = beanMethodExpression.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid method call format in @Authorization");
        }
        String beanName = parts[0];
        String methodName = parts[1];

        // Получаем параметры для метода через SpEL
        String parametersExpression = methodCall.substring(methodCall.indexOf('(') + 1, methodCall.indexOf(')'));
        String[] parameterExpressions = parametersExpression.split(",");
        Object[] methodParameters = new Object[parameterExpressions.length];

        for (int i = 0; i < parameterExpressions.length; i++) {
            methodParameters[i] = parser.parseExpression(parameterExpressions[i].trim()).getValue(context);
        }

        // Получаем бин и вызываем метод
        Object bean = applicationContext.getBean(beanName);
        Method method = bean.getClass().getMethod(methodName, getParameterTypes(methodParameters));
        return method.invoke(bean, methodParameters);
    }

    // Вспомогательный метод для получения типов параметров
    private Class<?>[] getParameterTypes(Object[] parameters) {
        Class<?>[] parameterTypes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        return parameterTypes;
    }
}
