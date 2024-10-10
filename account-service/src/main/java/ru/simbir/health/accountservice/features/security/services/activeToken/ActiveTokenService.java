package ru.simbir.health.accountservice.features.security.services.activeToken;

import ru.simbir.health.accountservice.features.security.entities.ActiveTokenEntity;

import java.util.UUID;

public interface ActiveTokenService {

    ActiveTokenEntity getById(UUID id);

    boolean existById(UUID id);

    UUID create();

    void deleteById(UUID id);
}
