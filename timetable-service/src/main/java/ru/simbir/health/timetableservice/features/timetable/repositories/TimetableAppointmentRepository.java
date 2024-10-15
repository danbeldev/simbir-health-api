package ru.simbir.health.timetableservice.features.timetable.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableAppointmentEntity;

import java.util.List;
import java.util.Optional;

public interface TimetableAppointmentRepository extends JpaRepository<TimetableAppointmentEntity, Long> {

    @Query("SELECT t FROM timetable_appointments t WHERE t.timetable.id = ?1 AND t.isDeleted = FALSE")
    List<TimetableAppointmentEntity> findAllActiveByTimetableId(Long timetableId);

    @Query("SELECT t FROM timetable_appointments t WHERE t.isDeleted = FALSE")
    List<TimetableAppointmentEntity> findAllActive();

    @Query("SELECT t FROM timetable_appointments t WHERE t.id = ?1 AND t.isDeleted = FALSE")
    Optional<TimetableAppointmentEntity> finActiveById(Long id);
}
