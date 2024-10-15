package ru.simbir.health.timetableservice.features.timetable.dto.params;

import java.time.Instant;

public record CreateOrUpdateTimetableParams(
        long hospitalId,
        long doctorId,
        Instant from,
        Instant to,
        String room
) {}
