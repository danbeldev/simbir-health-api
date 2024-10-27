package ru.simbir.health.hospitalservice.common.security.authenticate;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import ru.simbir.health.hospitalservice.common.security.context.SecurityContextHolder;
import ru.simbir.health.hospitalservice.common.security.user.models.UserRole;
import ru.simbir.health.hospitalservice.common.security.user.models.UserSessionDetails;
import ru.simbir.health.hospitalservice.features.user.client.UserServiceClient;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticateInterceptor implements HandlerInterceptor {

    private final UserServiceClient userServiceClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod method) {
            var authenticate = getAuthenticateAnnotation(method);

            if (authenticate != null) {
                var userDetails = authenticate(request, response);
                if (userDetails == null) {
                    return false;
                }

                if (!validation(userDetails, authenticate, method, request)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }

                var customContext = SecurityContextHolder.getContext();
                customContext.setUserSession(userDetails);
            }
        }

        return true;
    }

    private UserSessionDetails authenticate(HttpServletRequest request, HttpServletResponse response) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) return null;
        try {
            var userModel = userServiceClient.validationAccessToken(authorizationHeader.substring(7));
            if (!userModel.isActive()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
            return userModel;
        } catch (FeignException ex) {
            handleFailedStatus(ex.status(), response);
            return null;
        }
    }

    private void handleFailedStatus(int status, HttpServletResponse response) {
        if (status == -1) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private Authenticate getAuthenticateAnnotation(HandlerMethod method) {
        var authenticate = method.getMethodAnnotation(Authenticate.class);

        if (authenticate == null) {
            return method.getBeanType().getAnnotation(Authenticate.class);
        }

        return authenticate;
    }

    private boolean validation(UserSessionDetails user, Authenticate authenticate, HandlerMethod method, HttpServletRequest request) {
        return switch (authenticate.operation()) {
            case OR -> {
                if (authenticate.roles().length > 0) {
                    yield validationRoles(user, authenticate);
                }
                if (!authenticate.parameterUserId().isEmpty()) {
                    yield validationId(user, authenticate, method, request);
                }
                yield true;
            }
            case AND -> {

                if (authenticate.roles().length > 0 && !validationRoles(user, authenticate)) {
                    yield false;
                }

                if (!authenticate.parameterUserId().isEmpty() && !validationId(user, authenticate, method, request)) {
                    yield false;
                }

                yield true;
            }
        };
    }

    private boolean validationRoles(UserSessionDetails user, Authenticate authenticate) {
        var requiredRoles = List.of(authenticate.roles());

        for (UserRole role : user.getRoles()) {
            if (requiredRoles.contains(role)) return true;
        }

        return false;
    }

    private boolean validationId(UserSessionDetails user, Authenticate authenticate, HandlerMethod method, HttpServletRequest request) {
        Object argValue = getArgumentByName(method, authenticate.parameterUserId(), request);

        if (argValue != null) {
            long arg;
            if (argValue instanceof String) {
                try {
                    arg = Long.parseLong((String) argValue);
                } catch (NumberFormatException e) {
                    return false;
                }
            } else if (argValue instanceof Long) {
                arg = (Long) argValue;
            } else {
                return false;
            }

            return user.getId().equals(arg);
        }

        return false;
    }

    private Object getArgumentByName(HandlerMethod handlerMethod, String paramName, HttpServletRequest request) {
        var parameters = handlerMethod.getMethod().getParameters();

        for (Parameter parameter : parameters) {
            if (parameter.getName().equals(paramName)) {
                var pathVariable = parameter.getAnnotation(PathVariable.class);

                if (pathVariable != null) {
                    var name = pathVariable.name().isEmpty() ? paramName : pathVariable.name();
                    return getPathVariableValue(name, request);
                } else {
                    var requestParam = parameter.getAnnotation(RequestParam.class);
                    if (requestParam != null) {
                        var name = requestParam.name().isEmpty() ? paramName : requestParam.name();
                        return getRequestParamValue(requestParam, name, request);
                    } else {
                        return getRequestParamValue(paramName, request);
                    }
                }
            }
        }

        throw new IllegalArgumentException("No argument found with name: " + paramName);
    }

    private Object getPathVariableValue(String paramName, HttpServletRequest request) {
        Object uriVariablesObj = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (uriVariablesObj instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
            Map<String, String> uriVariables = (Map<String, String>) uriVariablesObj;
            if (uriVariables.containsKey(paramName)) {
                return uriVariables.get(paramName);
            }
        }

        throw new IllegalArgumentException("No path variable found with name: " + paramName);
    }


    private Object getRequestParamValue(RequestParam requestParam, String paramName, HttpServletRequest request) {
        String[] paramValues = request.getParameterMap().get(paramName);
        if (paramValues != null && paramValues.length > 0) {
            return paramValues[0];
        }
        if (!requestParam.required()) {
            return requestParam.defaultValue().equals(ValueConstants.DEFAULT_NONE) ? null : requestParam.defaultValue();
        }
        throw new IllegalArgumentException("No request parameter found with name: " + paramName);
    }

    private Object getRequestParamValue(String paramName, HttpServletRequest request) {
        String[] paramValues = request.getParameterValues(paramName);
        if (paramValues != null && paramValues.length > 0) {
            return paramValues[0];
        }
        throw new IllegalArgumentException("No request parameter found with name: " + paramName);
    }
}
