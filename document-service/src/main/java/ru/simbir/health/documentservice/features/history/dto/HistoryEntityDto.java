package ru.simbir.health.documentservice.features.history.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * DTO for {@link ru.simbir.health.documentservice.features.history.entities.HistoryEntity}
 */
public record HistoryEntityDto(
        @NotNull Long id,
        @NotNull String roomName,
        @NotNull Long pacientId,
        @NotNull String data,
        @NotNull Instant date
) {}