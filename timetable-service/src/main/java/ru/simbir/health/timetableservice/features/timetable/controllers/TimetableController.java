package ru.simbir.health.timetableservice.features.timetable.controllers;

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
public class TimetableController {

    private final TimetableService timetableService;
    private final TimetableAppointmentService timetableAppointmentService;

    private final TimetableEntityMapper timetableEntityMapper;

    @Authenticate
    @GetMapping("/Hospital/{id}")
    public List<TimetableEntityDto> getAllByHospitalId(
            @PathVariable Long id,
            @RequestBody Instant from,
            @RequestBody Instant to
    ) {
        var params = GetAllTimetablesParams.builder().hospitalId(id).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/Doctor/{id}")
    public List<TimetableEntityDto> getAllByDoctorId(
            @PathVariable Long id,
            @RequestBody Instant from,
            @RequestBody Instant to
    ) {
        var params = GetAllTimetablesParams.builder().doctorId(id).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

    @Authenticate
    @GetMapping("/Hospital/{id}/Room/{room}")
    public List<TimetableEntityDto> getAllByHospitalIdAndRoom(
            @PathVariable Long id,
            @PathVariable String room,
            @RequestBody Instant from,
            @RequestBody Instant to
    ) {
        var params = GetAllTimetablesParams.builder().hospitalId(id).room(room).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

    @PostMapping
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    @ValidHospitalAndRoom(hospitalIdFieldName = "#params.hospitalId", roomFieldName = "#params.room")
    public TimetableEntityDto create(
            @Valid @RequestBody CreateOrUpdateTimetableParams params
    ) {
        return timetableEntityMapper.toDto(timetableService.create(params));
    }

    @PutMapping("{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    public void update(
            @PathVariable Long id,
            @Valid @RequestBody CreateOrUpdateTimetableParams params
    ) {
        timetableService.update(id, params);
    }

    @DeleteMapping("/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    public void deleteById(@PathVariable Long id) {
        timetableService.softDeleteById(id);
    }

    @DeleteMapping("/Doctor/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    public void deleteByDoctorId(@PathVariable Long id) {
        timetableService.softDeleteByDoctorId(id);
    }

    @DeleteMapping("/Hospital/{id}")
    @Authenticate(roles = {UserRole.Admin, UserRole.Manager})
    public void deleteByHospitalId(@PathVariable Long id) {
        timetableService.softDeleteByHospitalId(id);
    }

    @Authenticate
    @GetMapping("/{id}/Appointments")
    public List<Instant> getAppointments(@PathVariable Long id) {
        return timetableAppointmentService.getAvailableAppointments(id);
    }

    @Authenticate
    @PostMapping("/{id}/Appointments")
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
    public void deleteAppointmentById(@PathVariable Long id, @UserSession UserSessionDetails userSession) {
        timetableAppointmentService.softDelete(id);
    }
}
