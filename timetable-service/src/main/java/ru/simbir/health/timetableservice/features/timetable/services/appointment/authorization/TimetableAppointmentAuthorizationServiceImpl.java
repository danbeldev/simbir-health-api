package ru.simbir.health.timetableservice.features.timetable.services.appointment.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simbir.health.timetableservice.common.security.user.models.UserRole;
import ru.simbir.health.timetableservice.features.timetable.services.appointment.TimetableAppointmentService;
import ru.simbir.health.timetableservice.features.user.client.models.UserModel;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableAppointmentAuthorizationServiceImpl implements TimetableAppointmentAuthorizationService {

    private final TimetableAppointmentService timetableAppointmentService;

    @Override
    @Transactional(readOnly = true)
    public boolean accessDeleteAppointmentById(Long id, UserModel userSession) {
        if (userSession.getRoles().containsAll(List.of(UserRole.Admin, UserRole.Manager)))
            return true;

        var timetable = timetableAppointmentService.getById(id);
        return timetable.getUserId().equals(userSession.getId());
    }
}
