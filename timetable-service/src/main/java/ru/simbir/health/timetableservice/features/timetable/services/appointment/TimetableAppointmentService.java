package ru.simbir.health.timetableservice.features.timetable.services.appointment;

import ru.simbir.health.timetableservice.features.timetable.entities.TimetableAppointmentEntity;

import java.time.Instant;
import java.util.List;

public interface TimetableAppointmentService {

    TimetableAppointmentEntity getById(long id);

    List<Instant> getAvailableAppointments(long timetableId);

    TimetableAppointmentEntity create(long userId, long timetableId, Instant time);

    void softDelete(long id);
}
