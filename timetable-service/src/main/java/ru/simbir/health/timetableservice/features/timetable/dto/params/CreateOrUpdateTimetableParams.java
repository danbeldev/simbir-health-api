package ru.simbir.health.timetableservice.features.timetable.dto.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.simbir.health.timetableservice.common.security.user.models.UserRole;
import ru.simbir.health.timetableservice.common.validate.user.ValidUser;

import java.time.Instant;

public record CreateOrUpdateTimetableParams(
        @NotNull(message = "{error.hospitalId.notnull}")
        Long hospitalId,

        @NotNull(message = "{error.doctorId.notnull}")
        @ValidUser(roles = UserRole.Doctor, message = "Invalid doctorId")
        Long doctorId,

        @NotNull(message = "{error.from.notnull}")
        Instant from,

        @NotNull(message = "{error.to.notnull}")
        Instant to,

        @Size(max = 48, message = "{error.room.tooLong}")
        @NotBlank(message = "{error.room.notblank}")
        String room
) {}
