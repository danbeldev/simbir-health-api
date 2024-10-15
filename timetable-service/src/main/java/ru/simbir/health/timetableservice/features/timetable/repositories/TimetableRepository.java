package ru.simbir.health.timetableservice.features.timetable.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.simbir.health.timetableservice.features.timetable.entities.TimetableEntity;

import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Long>, JpaSpecificationExecutor<TimetableEntity> {

    @Query("SELECT t FROM timetables t WHERE t.isDeleted == FALSE")
    List<TimetableEntity> findAllActive();

    @Query("SELECT t FROM timetables t WHERE t.id = ?1 AND t.isDeleted == FALSE")
    Optional<TimetableEntity> finActiveById(Long id);
}
