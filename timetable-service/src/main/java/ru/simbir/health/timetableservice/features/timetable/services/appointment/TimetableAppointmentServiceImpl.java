package ru.simbir.health.timetableservice.features.timetable.services.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableAppointmentEntity;
import ru.simbir.health.timetableservice.features.timetable.repositories.TimetableAppointmentRepository;
import ru.simbir.health.timetableservice.features.timetable.services.TimetableService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableAppointmentServiceImpl implements TimetableAppointmentService {

    private final TimetableAppointmentRepository timetableAppointmentRepository;

    private final TimetableService timetableService;

    @Override
    @Transactional(readOnly = true)
    public TimetableAppointmentEntity getById(long id) {
        return timetableAppointmentRepository.finActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instant> getAvailableAppointments(long timetableId) {
        var timetable = timetableService.getById(timetableId);

        List<Instant> bookedAppointments = timetableAppointmentRepository.findAllActiveByTimetableId(timetableId)
                .stream()
                .map(TimetableAppointmentEntity::getTime)
                .toList();

        List<Instant> availableAppointments = new ArrayList<>();
        Instant currentTime = timetable.getFrom();
        while (currentTime.isBefore(timetable.getTo())) {
            if (!bookedAppointments.contains(currentTime)) {
                availableAppointments.add(currentTime);
            }
            currentTime = currentTime.plus(30, ChronoUnit.MINUTES);
        }
        return availableAppointments;
    }

    @Override
    @Transactional
    public TimetableAppointmentEntity create(long userId, long timetableId, Instant time) {
        var timetable = timetableService.getById(timetableId);
        var appointment = new TimetableAppointmentEntity();
        appointment.setTime(time);
        appointment.setId(userId);
        appointment.setTimetable(timetable);
        return timetableAppointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void softDelete(long id) {
        var appointment = getById(id);
        appointment.setIsDeleted(true);
        timetableAppointmentRepository.save(appointment);
    }
}
