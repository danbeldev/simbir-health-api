package ru.simbir.health.timetableservice.features.timetable.dto.params;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateAppointmentParams(
        @NotNull(message = "{error.time.notnull}")
        Instant time
) {}
