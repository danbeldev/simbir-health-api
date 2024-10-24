package ru.simbir.health.documentservice.common.validate.hospital;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.documentservice.common.message.LocalizedMessageService;
import ru.simbir.health.documentservice.features.hospital.client.HospitalServiceClient;

import static ru.simbir.health.documentservice.common.spel.SpelContextUtils.createRequestContext;

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

        if (!hospitalServiceClient.validationHospitalAndRoom(hospitalId, room))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, localizedMessageService.getMessage("error.hospital.or.room.notfound"));

        return joinPoint.proceed();
    }
}
