package ru.simbir.health.documentservice.features.history.dto.params;

import ru.simbir.health.documentservice.common.validate.user.ValidUser;
import ru.simbir.health.documentservice.common.security.user.models.UserRole;

import java.time.Instant;

public record CreateOrUpdateParams(
        Instant date,
        @ValidUser(roles = UserRole.User, message = "Pacient not found")
        Long pacientId,
        Long hospitalId,
        @ValidUser(roles = UserRole.Doctor, message = "Doctor not found")
        Long doctorId,
        String room,
        String data
) {}
