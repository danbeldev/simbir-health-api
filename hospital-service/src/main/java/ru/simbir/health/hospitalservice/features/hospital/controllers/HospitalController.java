package ru.simbir.health.hospitalservice.features.hospital.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.hospitalservice.common.security.authenticate.Authenticate;
import ru.simbir.health.hospitalservice.common.security.user.models.UserRole;
import ru.simbir.health.hospitalservice.common.validate.pagination.PaginationLimit;
import ru.simbir.health.hospitalservice.common.validate.pagination.PaginationOffset;
import ru.simbir.health.hospitalservice.common.validate.pagination.ValidPagination;
import ru.simbir.health.hospitalservice.features.hospital.dto.HospitalEntityDto;
import ru.simbir.health.hospitalservice.features.hospital.dto.params.CreateOrUpdateHospitalParams;
import ru.simbir.health.hospitalservice.features.hospital.dto.room.HospitalRoomEntityDto;
import ru.simbir.health.hospitalservice.features.hospital.mappers.HospitalEntityMapper;
import ru.simbir.health.hospitalservice.features.hospital.mappers.HospitalRoomEntityMapper;
import ru.simbir.health.hospitalservice.features.hospital.services.HospitalService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Hospitals")
@Tag(name = "Больницы", description = "Эндпоинты для управления больницами.")
public class HospitalController {

    private final HospitalService hospitalService;

    private final HospitalEntityMapper hospitalEntityMapper;
    private final HospitalRoomEntityMapper hospitalRoomEntityMapper;

    @GetMapping
    @Authenticate
    @ValidPagination
    @Operation(summary = "Получить список больниц", description = "Возвращает список всех больниц с учетом пагинации.")
    public List<HospitalEntityDto> getAll(
            @PaginationOffset @RequestParam(defaultValue = "0") int from,
            @PaginationLimit @RequestParam(defaultValue = "20") int count
    ) {
        return hospitalService.getAll(from, count).getContent().stream().map(hospitalEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/{id}")
    @Operation(summary = "Получить больницу по ID", description = "Возвращает информацию о больнице по заданному ID.")
    public HospitalEntityDto getById(
            @PathVariable long id
    ) {
        return hospitalEntityMapper.toDto(hospitalService.getById(id));
    }

    @Authenticate
    @GetMapping("/{id}/Rooms")
    @Operation(summary = "Получить комнаты больницы", description = "Возвращает список всех комнат в больнице по заданному ID.")
    public List<HospitalRoomEntityDto> getRooms(
            @PathVariable long id
    ) {
        return hospitalService.getAllRoomsByHospitalId(id).stream().map(hospitalRoomEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/{hospitalId}/Room/Validation")
    @Operation(summary = "Проверка валидности больницы и комнаты", description = "Проверяет, существует ли указанная комната в заданной больнице.")
    boolean validationHospitalAndRoom(
            @PathVariable long hospitalId,
            @RequestParam String room
    ) {
        return hospitalService.validationHospitalAndRoom(hospitalId, room);
    }

    @PostMapping
    @Authenticate(roles = UserRole.Admin)
    @Operation(summary = "Создать новую больницу", description = "Создаёт новую запись о больнице с заданными параметрами.")
    public HospitalEntityDto create(
            @Valid @RequestBody CreateOrUpdateHospitalParams params
    ) {
        return hospitalEntityMapper.toDto(hospitalService.create(params));
    }

    @PutMapping("/{id}")
    @Authenticate(roles = UserRole.Admin)
    @Operation(summary = "Обновить информацию о больнице", description = "Обновляет данные больницы по заданному ID.")
    public void update(
            @PathVariable long id,
            @Valid @RequestBody CreateOrUpdateHospitalParams params
    ) {
        hospitalService.update(id, params);
    }

    @DeleteMapping("/{id}")
    @Authenticate(roles = UserRole.Admin)
    @Operation(summary = "Удалить больницу", description = "Удаляет запись о больнице по заданному ID.")
    public void delete(
            @PathVariable long id
    ) {
        hospitalService.softDelete(id);
    }
}
