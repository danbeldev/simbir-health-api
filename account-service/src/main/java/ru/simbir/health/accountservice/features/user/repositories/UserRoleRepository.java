package ru.simbir.health.accountservice.features.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleEntityId> {}
