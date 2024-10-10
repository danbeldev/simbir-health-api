package ru.simbir.health.accountservice.features.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.simbir.health.accountservice.features.security.entities.ActiveTokenEntity;

import java.util.UUID;

public interface ActiveTokenRepository extends JpaRepository<ActiveTokenEntity, UUID> {

}
