package ru.simbir.health.accountservice.features.user.repositories;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.isDeleted = false")
    Optional<UserEntity> findActiveById(long id);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.isDeleted = false")
    Optional<UserEntity> findActiveByUsername(String username);

    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.isDeleted = false")
    Optional<UserEntity> findActiveByUsernameWithRoles(String username);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.id != :userId AND u.isDeleted = false")
    Optional<UserEntity> findActiveByUsername(String username, Long userId);

    @Query("SELECT u FROM UserEntity u WHERE u.isDeleted = false")
    Slice<UserEntity> findSliceOfActiveUsers(Pageable pageable);
}