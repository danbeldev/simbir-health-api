package ru.simbir.health.timetableservice.features.timetable.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.timetableservice.features.timetable.dto.params.CreateAppointmentParams;
import ru.simbir.health.timetableservice.features.timetable.dto.TimetableEntityDto;
import ru.simbir.health.timetableservice.features.timetable.dto.params.CreateOrUpdateTimetableParams;
import ru.simbir.health.timetableservice.features.timetable.dto.params.GetAllTimetablesParams;
import ru.simbir.health.timetableservice.features.timetable.mappers.TimetableEntityMapper;
import ru.simbir.health.timetableservice.features.timetable.services.TimetableService;
import ru.simbir.health.timetableservice.features.timetable.services.appointment.TimetableAppointmentService;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Timetable")
public class TimetableController {

    private final TimetableService timetableService;
    private final TimetableAppointmentService timetableAppointmentService;

    private final TimetableEntityMapper timetableEntityMapper;

    @GetMapping("/Hospital/{id}")
    public List<TimetableEntityDto> getAllByHospitalId(
            @PathVariable Long id,
            @RequestBody Instant from,
            @RequestBody Instant to
    ) {
        var params = GetAllTimetablesParams.builder().hospitalId(id).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

    @GetMapping("/Doctor/{id}")
    public List<TimetableEntityDto> getAllByDoctorId(
            @PathVariable Long id,
            @RequestBody Instant from,
            @RequestBody Instant to
    ) {
        var params = GetAllTimetablesParams.builder().doctorId(id).from(from).to(to).build();
        return timetableService.getAll(params).stream().map(timetableEntityMapper::toDto).toList();
    }

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
    public TimetableEntityDto create(
            @RequestBody CreateOrUpdateTimetableParams params
    ) {
        return timetableEntityMapper.toDto(timetableService.create(params));
    }

    @PutMapping("{id}")
    public void update(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateTimetableParams params
    ) {
        timetableService.update(id, params);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        timetableService.softDeleteById(id);
    }

    @DeleteMapping("/Doctor/{id}")
    public void deleteByDoctorId(@PathVariable Long id) {
        timetableService.softDeleteByDoctorId(id);
    }

    @DeleteMapping("/Hospital/{id}")
    public void deleteByHospitalId(@PathVariable Long id) {
        timetableService.softDeleteByHospitalId(id);
    }

    @GetMapping("/{id}/Appointments")
    public void getAppointments(@PathVariable Long id) {
        timetableAppointmentService.getAvailableAppointments(id);
    }

    @PostMapping("/{id}/Appointments")
    public void createAppointment(@PathVariable Long id, @RequestBody CreateAppointmentParams dto) {
        // TODO: UserId
        timetableAppointmentService.create(1, id, dto.time());
    }

    @DeleteMapping("/Appointments/{id}")
    public void deleteAppointmentById(@PathVariable Long id) {
        timetableAppointmentService.softDelete(id);
    }
}
