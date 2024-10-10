package ru.simbir.health.accountservice.features.security.dto.params;

public record SignInParams(
        String username,
        String password
) {}
