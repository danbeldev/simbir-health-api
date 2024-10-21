package ru.simbir.health.hospitalservice.features.hospital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalRoomEntity;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalRoomEntityId;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoomEntity, HospitalRoomEntityId> {}
