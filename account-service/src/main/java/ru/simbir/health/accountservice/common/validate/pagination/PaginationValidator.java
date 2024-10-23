package ru.simbir.health.accountservice.common.validate.pagination;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.accountservice.common.message.LocalizedMessageService;

@Aspect
@Component
@RequiredArgsConstructor
public class PaginationValidator {

    @Value("${base.min-offset}")
    private Integer minOffset;

    @Value("${base.min-limit}")
    private Integer minLimit;

    @Value("${base.max-limit}")
    private Integer maxLimit;

    private final LocalizedMessageService localizedMessageService;

    @Around("@annotation(ValidPagination)")
    public Object validatePagination(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        var args = joinPoint.getArgs();

        int offset = -1;
        int limit = -1;

        for (int i = 0; i < signature.getMethod().getParameterAnnotations().length; i++) {
            for (var annotation : signature.getMethod().getParameterAnnotations()[i]) {
                if (annotation instanceof PaginationOffset) {
                    offset = (int) args[i];
                } else if (annotation instanceof PaginationLimit) {
                    limit = (int) args[i];
                }
            }
        }

        if (offset < minOffset) {
            var message = localizedMessageService.getMessage("pagination.offset.invalid", offset, minOffset);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        if (limit < minLimit) {
            var message = localizedMessageService.getMessage("pagination.limit.invalid.min", limit, minLimit);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        if (limit > maxLimit) {
            var message = localizedMessageService.getMessage("pagination.limit.invalid.max", limit, maxLimit);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        return joinPoint.proceed();
    }
}
