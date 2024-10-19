package ru.simbir.health.timetableservice.features.timetable.services.appointment.authorization;

import ru.simbir.health.timetableservice.features.user.client.models.UserModel;

public interface TimetableAppointmentAuthorizationService {

    boolean accessDeleteAppointmentById(Long id, UserModel userSession);
}
