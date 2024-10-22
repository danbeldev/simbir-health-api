package ru.simbir.health.timetableservice.features.timetable.dto.params;

import ru.simbir.health.timetableservice.common.security.user.models.UserRole;
import ru.simbir.health.timetableservice.common.validate.user.ValidUser;

import java.time.Instant;

public record CreateOrUpdateTimetableParams(
        Long hospitalId,
        @ValidUser(roles = UserRole.Doctor, message = "Invalid doctorId")
        Long doctorId,
        Instant from,
        Instant to,
        String room
) {}
