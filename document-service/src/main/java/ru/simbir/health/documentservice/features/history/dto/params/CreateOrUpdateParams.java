package ru.simbir.health.documentservice.features.history.dto.params;

import java.time.Instant;

public record CreateOrUpdateParams(
        Instant date,
        Long pacientId,
        Long hospitalId,
        Long doctorId,
        String room,
        String data
) {}
