package ru.simbir.health.timetableservice.features.timetable.dto;

import java.time.Instant;

/**
 * DTO for {@link ru.simbir.health.timetableservice.features.timetable.entities.TimetableEntity}
 */
public record TimetableEntityDto(Long id, Instant from, Instant to, Long doctorId, Long hospitalId, String roomName) {
}