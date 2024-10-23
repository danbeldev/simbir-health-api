package ru.simbir.health.accountservice.features.security.dto.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInParams(
        @NotBlank(message = "{user.username.notblank}")
        @Size(max = 128, message = "{user.username.size}")
        String username,

        @NotBlank(message = "{user.password.notblank}")
        @Size(max = 128, message = "{user.password.size}")
        String password
) {}
