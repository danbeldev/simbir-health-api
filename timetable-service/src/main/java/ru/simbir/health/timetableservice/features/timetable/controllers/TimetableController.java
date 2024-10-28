package ru.simbir.health.timetableservice.features.timetable.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.timetableservice.common.security.authenticate.Authenticate;
import ru.simbir.health.timetableservice.common.security.authorization.Authorization;
import ru.simbir.health.timetableservice.common.security.user.UserSession;
import ru.simbir.health.timetableservice.common.security.user.models.UserRole;
import ru.simbir.health.timetableservice.common.security.user.models.UserSessionDetails;
import ru.simbir.health.timetableservice.common.validate.hospital.ValidHospitalAndRoom;
import ru.simbir.health.timetableservice.features.timetable.dto.params.CreateAppointmentParams;
import ru.simbir.health.timetableservice.features.timetable.dto.TimetableEntityDto;
import ru.simbir.health.timetableservice.features.timetable.dto.params.CreateOrUpdateTimetableParams;
import ru.simbir.health.timetableservice.features.timetable.dto.params.GetAllTimetablesParams;
import ru.simbir.health.timetableservice.features.timetable.mappers.TimetableEntityMapper;
import ru.simbir.health.timetableservice.features.timetable.services.TimetableService;
import ru.simbir.health.timetableservice.features.timetable.services.appointment.TimetableAppointmentService;

import java.time.Instant;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Timetable")
@Tag(name = "Расписание", description = "Эндпоинты для управления расписанием.")
public class TimetableController {

    private final TimetableService timetableService;
    private final TimetableAppointmentService timetableAppointmentService;

    private final TimetableEntityMapper timetableEntityMapper;

    @Authenticate
    @GetMapping("/Hospital/{id}")
    @Operation(summary = "Получить расписание больницы",
            description = "Возвращает расписание больницы по заданному ID.")
    public List<TimetableEntityDto> getAllByHospitalId(
            @PathVariable Long id,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @UserSession UserSessionDetails userSessionDetails
    ) {
        var params = GetAllTimetablesParams.builder().hospitalId(id).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/Doctor/{id}")
    @Operation(summary = "Получить расписание врача",
            description = "Возвращает расписание врача по заданному ID.")
    public List<TimetableEntityDto> getAllByDoctorId(
            @PathVariable Long id,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to
    ) {
        var params = GetAllTimetablesParams.builder().doctorId(id).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/Hospital/{id}/Room/{room}")
    @Operation(summary = "Получить расписание кабинета больницы",
            description = "Возвращает расписание кабинета больницы по заданному ID и комнате.")
    public List<TimetableEntityDto> getAllByHospitalIdAndRoom(
            @PathVariable Long id,
            @PathVariable String room,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to
    ) {
        var params = GetAllTimetablesParams.builder().hospitalId(id).room(room).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

    @PostMapping
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    @ValidHospitalAndRoom(hospitalIdFieldName = "#params.hospitalId", roomFieldName = "#params.room")
    @Operation(summary = "Создать запись в расписании",
            description = "Создаёт новую запись в расписании.")
    public TimetableEntityDto create(
            @Valid @RequestBody CreateOrUpdateTimetableParams params
    ) {
        return timetableEntityMapper.toDto(timetableService.create(params));
    }

    @PutMapping("{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    @Operation(summary = "Обновить запись в расписании",
            description = "Обновляет запись в расписании по заданному ID.")
    public void update(
            @PathVariable Long id,
            @Valid @RequestBody CreateOrUpdateTimetableParams params
    ) {
        timetableService.update(id, params);
    }

    @DeleteMapping("/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    @Operation(summary = "Удалить запись расписания",
            description = "Удаляет запись расписания по заданному ID.")
    public void deleteById(@PathVariable Long id) {
        timetableService.softDeleteById(id);
    }

    @DeleteMapping("/Doctor/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    @Operation(summary = "Удалить записи расписания врача",
            description = "Удаляет все записи расписания для врача по заданному ID.")
    public void deleteByDoctorId(@PathVariable Long id) {
        timetableService.softDeleteByDoctorId(id);
    }

    @DeleteMapping("/Hospital/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    @Operation(summary = "Удалить записи расписания больницы",
            description = "Удаляет все записи расписания для больницы по заданному ID.")
    public void deleteByHospitalId(@PathVariable Long id) {
        timetableService.softDeleteByHospitalId(id);
    }

    @Authenticate
    @GetMapping("/{id}/Appointments")
    @Operation(summary = "Получить свободные талоны на приём",
            description = "Возвращает доступные талончики на приём для указанного расписания.")
    public List<Instant> getAppointments(@PathVariable Long id) {
        return timetableAppointmentService.getAvailableAppointments(id);
    }

    @Authenticate
    @PostMapping("/{id}/Appointments")
    @Operation(summary = "Записаться на приём",
            description = "Создаёт запись на приём по указанному времени.")
    public long createAppointment(
            @PathVariable Long id,
            @Valid @RequestBody CreateAppointmentParams dto,
            @UserSession UserSessionDetails userSession
    ) {
        return timetableAppointmentService.create(userSession.getId(), id, dto.time()).getId();
    }

    @Authenticate
    @DeleteMapping("/Appointments/{id}")
    @Authorization(value = "timetableAppointmentAuthorizationService.accessDeleteAppointmentById(#id, #userSession)")
    @Operation(summary = "Отменить запись на приём",
            description = "Удаляет запись на приём по заданному ID.")
    public void deleteAppointmentById(@PathVariable Long id, @UserSession UserSessionDetails userSession) {
        timetableAppointmentService.softDelete(id);
    }
}
