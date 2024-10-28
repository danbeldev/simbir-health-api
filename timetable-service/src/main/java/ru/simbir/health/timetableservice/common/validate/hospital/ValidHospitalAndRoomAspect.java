package ru.simbir.health.timetableservice.common.validate.hospital;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.timetableservice.common.message.LocalizedMessageService;
import ru.simbir.health.timetableservice.common.security.context.SecurityContextHolder;
import ru.simbir.health.timetableservice.features.hospital.client.HospitalServiceClient;

import static ru.simbir.health.timetableservice.common.spel.SpelContextUtils.createRequestContext;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidHospitalAndRoomAspect {

    private final ExpressionParser parser = new SpelExpressionParser();

    private final HospitalServiceClient hospitalServiceClient;

    private final LocalizedMessageService localizedMessageService;

    @Around("@annotation(validHospitalAndRoom)")
    public Object validation(ProceedingJoinPoint joinPoint, ValidHospitalAndRoom validHospitalAndRoom) throws Throwable {
        var context = createRequestContext(joinPoint);

        var hospitalId = parser.parseExpression(validHospitalAndRoom.hospitalIdFieldName()).getValue(context, Long.class);
        var room = parser.parseExpression(validHospitalAndRoom.roomFieldName()).getValue(context, String.class);

        try {
            if (!hospitalServiceClient.validationHospitalAndRoom(hospitalId, room, SecurityContextHolder.getContext().getAccessToken()))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, localizedMessageService.getMessage("error.hospital.or.room.notfound"));
        } catch (FeignException ex) {
            if (ex.status() == -1) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
            } else {
                throw ex;
            }
        }

        return joinPoint.proceed();
    }
}
