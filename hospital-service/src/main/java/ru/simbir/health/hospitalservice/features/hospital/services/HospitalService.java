package ru.simbir.health.hospitalservice.features.hospital.services;

import org.springframework.data.domain.Slice;
import ru.simbir.health.hospitalservice.features.hospital.dto.params.CreateOrUpdateHospitalParams;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalEntity;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalRoomEntity;

import java.util.Set;

public interface HospitalService {

    HospitalEntity getById(long id);

    Slice<HospitalEntity> getAll(int from, int count);

    Set<HospitalRoomEntity> getAllRoomsByHospitalId(long id);

    HospitalEntity create(CreateOrUpdateHospitalParams params);

    void update(long id, CreateOrUpdateHospitalParams params);

    void softDelete(long id);

    boolean validationHospitalAndRoom(long hospitalId, String room);
}
