package ru.simbir.health.hospitalservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.simbir.health.hospitalservice.entities.HospitalRoomEntity;

import java.util.List;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoomEntity, Long> {

    List<HospitalRoomEntity> findAllByHospitalId(long hospitalId);
}
