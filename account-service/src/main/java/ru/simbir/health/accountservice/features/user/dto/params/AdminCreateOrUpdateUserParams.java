package ru.simbir.health.accountservice.features.user.dto.params;

import java.util.List;

public record AdminCreateOrUpdateUserParams(
        String lastName,
        String firstName,
        String username,
        String password,
        List<String> roles
) {}