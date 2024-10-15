package ru.simbir.health.hospitalservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.simbir.health.hospitalservice.entities.HospitalRoomEntity;
import ru.simbir.health.hospitalservice.entities.HospitalRoomEntityId;

import java.util.List;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoomEntity, HospitalRoomEntityId> {

    @Query("SELECT h FROM hospital_rooms h WHERE h.id.hospitalId = ?1")
    List<HospitalRoomEntity> findAllByHospitalId(long hospitalId);
}
