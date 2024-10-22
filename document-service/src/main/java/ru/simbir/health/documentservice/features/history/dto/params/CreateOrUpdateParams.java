package ru.simbir.health.documentservice.features.history.dto.params;

import ru.simbir.health.documentservice.common.validate.user.ValidUser;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;

import java.time.Instant;

public record CreateOrUpdateParams(
        Instant date,
        @ValidUser(roles = UserRole.User, message = "Invalid pacientId")
        Long pacientId,
        Long hospitalId,
        @ValidUser(roles = UserRole.Doctor, message = "Invalid doctorId")
        Long doctorId,
        String room,
        String data
) {}
