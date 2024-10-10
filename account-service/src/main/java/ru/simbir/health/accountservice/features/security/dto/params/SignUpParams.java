package ru.simbir.health.accountservice.features.security.dto.params;

public record SignUpParams(
        String lastName,
        String firstName,
        String username,
        String password
) {}
