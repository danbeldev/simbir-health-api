package ru.simbir.health.hospitalservice.features.hospital.dto;

import ru.simbir.health.hospitalservice.features.hospital.entities.HospitalEntity;

/**
 * DTO for {@link HospitalEntity}
 */
public record HospitalEntityDto(Long id, String name, String address, String contactPhone) {
}