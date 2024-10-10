package ru.simbir.health.accountservice.features.user.dto;

/**
 * DTO for {@link ru.simbir.health.accountservice.features.user.entities.UserEntity}
 */
public record UserEntityDto(Long id, String lastName, String firstName, String username) {
}