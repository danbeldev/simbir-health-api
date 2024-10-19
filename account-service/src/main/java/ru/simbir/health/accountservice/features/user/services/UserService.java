package ru.simbir.health.accountservice.features.user.services;

import org.springframework.data.domain.Slice;
import ru.simbir.health.accountservice.features.user.dto.params.AdminCreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.params.CreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;

import java.util.Collection;

public interface UserService {

    UserEntity getById(long id);

    UserEntity getById(long id, UserRoleEntityId.Role role);

    UserEntity getByUsername(String username);

    UserEntity getByUsernameWithRoles(String username);

    Slice<UserEntity> getAll(int offset, int limit);

    Slice<UserEntity> getAll(int offset, int limit, String nameFilter, UserRoleEntityId.Role role);

    void update(long id, AdminCreateOrUpdateUserParams params);

    void update(long id, CreateOrUpdateUserParams params);

    void update(UserEntity user, boolean passwordEncoding);

    UserEntity create(AdminCreateOrUpdateUserParams params);

    UserEntity create(UserEntity user);

    void softDelete(long id);

    boolean isExists(long id, Collection<UserRoleEntityId.Role> roles, boolean requireAll);
}
