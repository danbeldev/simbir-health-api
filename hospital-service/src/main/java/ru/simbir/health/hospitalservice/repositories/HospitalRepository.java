package ru.simbir.health.hospitalservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.simbir.health.hospitalservice.entities.HospitalEntity;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

    @Query("SELECT h FROM hospitals h WHERE h.id = :id AND h.isDeleted = false")
    Optional<HospitalEntity> findActiveById(long id);

    @Query("SELECT h FROM hospitals h WHERE h.isDeleted = false")
    Slice<HospitalEntity> findSliceOfActiveHospitals(Pageable pageable);

    @EntityGraph(attributePaths = {"rooms"})
    @Query("SELECT h FROM hospitals h WHERE h.id = :id AND h.isDeleted = false")
    Optional<HospitalEntity> findActiveByIdWithRooms(long id);
}
