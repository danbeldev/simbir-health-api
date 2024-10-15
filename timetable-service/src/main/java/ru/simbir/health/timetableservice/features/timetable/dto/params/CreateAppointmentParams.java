package ru.simbir.health.timetableservice.features.timetable.dto.params;

import java.time.Instant;

public record CreateAppointmentParams(
        Instant time
) {
}
