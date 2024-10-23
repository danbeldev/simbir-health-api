package ru.simbir.health.accountservice.features.user.dto.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

import java.util.List;

public record AdminCreateOrUpdateUserParams(
        @NotBlank(message = "{user.lastname.notblank}")
        @Size(max = 128, message = "{user.lastname.size}")
        String lastName,

        @NotBlank(message = "{user.firstname.notblank}")
        @Size(max = 128, message = "{user.firstname.size}")
        String firstName,

        @NotBlank(message = "{user.username.notblank}")
        @Size(max = 128, message = "{user.username.size}")
        String username,

        @NotBlank(message = "{user.password.notblank}")
        @Size(max = 128, message = "{user.password.size}")
        String password,

        List<UserRoleEntityId.Role> roles
) {}