package ru.simbir.health.hospitalservice.features.hospital.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.hospitalservice.features.hospital.dto.params.CreateOrUpdateHospitalParams;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalEntity;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalRoomEntity;
import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalRoomEntityId;
import ru.simbir.health.hospitalservice.features.hospital.repositories.HospitalRepository;
import ru.simbir.health.hospitalservice.features.hospital.repositories.HospitalRoomRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    private final HospitalRoomRepository hospitalRoomRepository;

    @Override
    @Transactional(readOnly = true)
    public HospitalEntity getById(long id) {
        return hospitalRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<HospitalEntity> getAll(int from, int count) {
        return hospitalRepository.findSliceOfActiveHospitals(PageRequest.of(from, count));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<HospitalRoomEntity> getAllRoomsByHospitalId(long id) {
        var hospital = hospitalRepository.findActiveByIdWithRooms(id);

        if (hospital.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found with id: " + id);

        return hospital.get().getRooms();
    }

    @Override
    @Transactional
    public HospitalEntity create(CreateOrUpdateHospitalParams params) {
        final var hospital = new HospitalEntity();
        hospital.setName(params.name());
        hospital.setAddress(params.address());
        hospital.setContactPhone(params.contactPhone());
        final var savedHospital = hospitalRepository.save(hospital);
        updateRooms(savedHospital, params.rooms());
        return savedHospital;
    }

    @Override
    @Transactional
    public void update(long id, CreateOrUpdateHospitalParams params) {
        final var hospital = getById(id);
        hospital.setName(params.name());
        hospital.setAddress(params.address());
        hospital.setContactPhone(params.contactPhone());
        hospitalRepository.save(hospital);
        updateRooms(hospital, params.rooms());
    }

    @Override
    @Transactional
    public void softDelete(long id) {
        var hospital = getById(id);
        hospital.setIsDeleted(true);
        hospitalRepository.save(hospital);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = ResponseStatusException.class)
    public boolean validationHospitalAndRoom(long hospitalId, String roomName) {
        Set<HospitalRoomEntity> rooms;

        try {
            rooms = getAllRoomsByHospitalId(hospitalId);
        } catch (ResponseStatusException e) {
            return false;
        }

        for (HospitalRoomEntity room : rooms) {
            if (room.getId().getName().equals(roomName)) return true;
        }

        return false;
    }

    private void updateRooms(HospitalEntity hospital, List<String> rooms) {
        if (rooms != null && !rooms.isEmpty()) {
            Set<String> existingRoomNames = hospital.getRooms().stream()
                    .map(h -> h.getId().getName())
                    .collect(Collectors.toSet());

            List<HospitalRoomEntity> roomsToAdd = rooms.stream()
                    .filter(roomName -> !existingRoomNames.contains(roomName))
                    .map(roomName -> {
                        var hospitalRoomEntity = new HospitalRoomEntity();
                        var id = new HospitalRoomEntityId();
                        id.setHospital(hospital);
                        id.setName(roomName);
                        hospitalRoomEntity.setId(id);
                        return hospitalRoomEntity;
                    })
                    .collect(Collectors.toList());

            List<HospitalRoomEntity> roomsToRemove = hospital.getRooms().stream()
                    .filter(room -> !rooms.contains(room.getId().getName()))
                    .collect(Collectors.toList());

            if (!roomsToAdd.isEmpty()) {
                hospitalRoomRepository.saveAll(roomsToAdd);
            }

            if (!roomsToRemove.isEmpty()) {
                hospitalRoomRepository.deleteAll(roomsToRemove);
            }
        }
    }
}
