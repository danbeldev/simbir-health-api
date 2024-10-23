package ru.simbir.health.accountservice.features.user.dto.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrUpdateUserParams(
        @NotBlank(message = "{user.lastname.notblank}")
        @Size(max = 128, message = "{user.lastname.size}")
        String lastName,

        @NotBlank(message = "{user.firstname.notblank}")
        @Size(max = 128, message = "{user.firstname.size}")
        String firstName,

        @NotBlank(message = "{user.password.notblank}")
        @Size(max = 128, message = "{user.password.size}")
        String password
) {}
