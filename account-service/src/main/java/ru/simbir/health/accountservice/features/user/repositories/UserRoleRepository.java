package ru.simbir.health.accountservice.features.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleEntityId> {

    @Modifying
    @Query("DELETE FROM UserRoleEntity ur WHERE ur.id.userId = :userId")
    void deleteAllByUserId(Long userId);
}
