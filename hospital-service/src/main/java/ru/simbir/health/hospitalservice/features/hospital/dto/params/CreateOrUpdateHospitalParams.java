package ru.simbir.health.hospitalservice.features.hospital.dto.params;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrUpdateHospitalParams(
        @NotBlank(message = "{error.name.notblank}")
        @Size(max = 256, message = "{error.name.tooLong}")
        String name,

        @NotBlank(message = "{error.address.notblank}")
        @Size(max = 512, message = "{error.address.tooLong}")
        String address,

        @NotBlank(message = "{error.contactPhone.notblank}")
        @Size(max = 32, message = "{error.contactPhone.tooLong}")
        String contactPhone,

        @Valid
        List<@NotBlank(message = "{error.room.notblank}") String> rooms
) {}