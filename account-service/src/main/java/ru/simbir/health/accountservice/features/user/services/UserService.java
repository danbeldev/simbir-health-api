package ru.simbir.health.accountservice.features.user.services;

import org.springframework.data.domain.Slice;
import ru.simbir.health.accountservice.features.user.dto.params.AdminCreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.params.CreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;

public interface UserService {

    UserEntity getById(long id);

    UserEntity getByUsername(String username);

    UserEntity getByUsernameWithRoles(String username);

    Slice<UserEntity> getAll(int offset, int limit);

    void update(long id, AdminCreateOrUpdateUserParams params);

    void update(long id, CreateOrUpdateUserParams params);

    void update(UserEntity user, boolean passwordEncoding);

    UserEntity create(AdminCreateOrUpdateUserParams params);

    UserEntity create(UserEntity user);

    void softDelete(long id);
}
