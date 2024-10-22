package ru.simbir.health.hospitalservice.features.hospital.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.hospitalservice.common.security.authenticate.Authenticate;
import ru.simbir.health.hospitalservice.common.security.user.models.UserRole;
import ru.simbir.health.hospitalservice.features.hospital.dto.HospitalEntityDto;
import ru.simbir.health.hospitalservice.features.hospital.dto.params.CreateOrUpdateHospitalParams;
import ru.simbir.health.hospitalservice.features.hospital.dto.room.HospitalRoomEntityDto;
import ru.simbir.health.hospitalservice.features.hospital.mappers.HospitalEntityMapper;
import ru.simbir.health.hospitalservice.features.hospital.mappers.HospitalRoomEntityMapper;
import ru.simbir.health.hospitalservice.features.hospital.services.HospitalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    private final HospitalEntityMapper hospitalEntityMapper;
    private final HospitalRoomEntityMapper hospitalRoomEntityMapper;

    @GetMapping
    @Authenticate
    public List<HospitalEntityDto> getAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int count
    ) {
        return hospitalService.getAll(from, count).getContent().stream().map(hospitalEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/{id}")
    public HospitalEntityDto getById(
            @PathVariable long id
    ) {
        return hospitalEntityMapper.toDto(hospitalService.getById(id));
    }

    @Authenticate
    @GetMapping("/{id}/Rooms")
    public List<HospitalRoomEntityDto> getRooms(
            @PathVariable long id
    ) {
        return hospitalService.getAllRoomsByHospitalId(id).stream().map(hospitalRoomEntityMapper::toDto).toList();
    }

    @Hidden
    @GetMapping("/{hospitalId}/Room/Validation")
    boolean validationHospitalAndRoom(
            @PathVariable long hospitalId,
            @RequestParam String room
    ) {
        return hospitalService.validationHospitalAndRoom(hospitalId, room);
    }

    @PostMapping
    @Authenticate(roles = UserRole.Admin)
    public HospitalEntityDto create(
            @RequestBody CreateOrUpdateHospitalParams params
    ) {
        return hospitalEntityMapper.toDto(hospitalService.create(params));
    }

    @PutMapping("/{id}")
    @Authenticate(roles = UserRole.Admin)
    public void update(
            @PathVariable long id,
            @RequestBody CreateOrUpdateHospitalParams params
    ) {
        hospitalService.update(id, params);
    }

    @DeleteMapping("/{id}")
    @Authenticate(roles = UserRole.Admin)
    public void delete(
            @PathVariable long id
    ) {
        hospitalService.softDelete(id);
    }
}
