package ru.simbir.health.accountservice.features.user.dto.params;

public record CreateOrUpdateUserParams(
        String lastName,
        String firstName,
        String password
) {}
