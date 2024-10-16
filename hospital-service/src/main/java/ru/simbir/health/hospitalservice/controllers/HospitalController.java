package ru.simbir.health.hospitalservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.hospitalservice.dto.HospitalEntityDto;
import ru.simbir.health.hospitalservice.dto.params.CreateOrUpdateHospitalParams;
import ru.simbir.health.hospitalservice.dto.room.HospitalRoomEntityDto;
import ru.simbir.health.hospitalservice.mappers.HospitalEntityMapper;
import ru.simbir.health.hospitalservice.mappers.HospitalRoomEntityMapper;
import ru.simbir.health.hospitalservice.services.HospitalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    private final HospitalEntityMapper hospitalEntityMapper;
    private final HospitalRoomEntityMapper hospitalRoomEntityMapper;

    @GetMapping
    public List<HospitalEntityDto> getAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int count
    ) {
        return hospitalService.getAll(from, count).getContent().stream().map(hospitalEntityMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public HospitalEntityDto getById(
            @PathVariable long id
    ) {
        return hospitalEntityMapper.toDto(hospitalService.getById(id));
    }

    @GetMapping("/{id}/Rooms")
    public List<HospitalRoomEntityDto> getRooms(
            @PathVariable long id
    ) {
        return hospitalService.getAllRoomsByHospitalId(id).stream().map(hospitalRoomEntityMapper::toDto).toList();
    }

    @GetMapping("/{hospitalId}/Room/{room}/Validation")
    boolean validationHospitalAndRoom(
            @PathVariable long hospitalId,
            @PathVariable String room
    ) {
        return hospitalService.validationHospitalAndRoom(hospitalId, room);
    }

    @PostMapping
    public HospitalEntityDto create(
            @RequestBody CreateOrUpdateHospitalParams params
    ) {
        return hospitalEntityMapper.toDto(hospitalService.create(params));
    }

    @PutMapping("/{id}")
    public void update(
            @PathVariable long id,
            @RequestBody CreateOrUpdateHospitalParams params
    ) {
        hospitalService.update(id, params);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable long id
    ) {
        hospitalService.softDelete(id);
    }
}
