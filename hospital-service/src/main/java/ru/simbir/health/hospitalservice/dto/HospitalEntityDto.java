package ru.simbir.health.hospitalservice.dto;

/**
 * DTO for {@link ru.simbir.health.hospitalservice.entities.HospitalEntity}
 */
public record HospitalEntityDto(Long id, String name, String address, String contactPhone) {
}