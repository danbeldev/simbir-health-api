package ru.simbir.health.documentservice.features.history.dto.params;

import jakarta.validation.constraints.NotBlank;
import ru.simbir.health.documentservice.common.validate.user.ValidUser;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrUpdateHistoryParams(
        @NotNull(message = "{error.date.notnull}")
        Instant date,

        @NotNull(message = "{error.pacientId.notnull}")
        @ValidUser(roles = UserRole.User, message = "{error.pacientId.invalid}")
        Long pacientId,

        @NotNull(message = "{error.hospitalId.notnull}")
        Long hospitalId,

        @NotNull(message = "{error.doctorId.notnull}")
        @ValidUser(roles = UserRole.Doctor, message = "{error.doctorId.invalid}")
        Long doctorId,

        @NotBlank(message = "{error.room.notnull}")
        @Size(max = 48, message = "{error.room.tooLong}")
        String room,

        @NotBlank(message = "{error.data.notnull}")
        @Size(max = 2048, message = "{error.data.tooLong}")
        String data
) {}