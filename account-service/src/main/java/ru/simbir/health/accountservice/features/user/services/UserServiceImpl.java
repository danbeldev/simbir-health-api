package ru.simbir.health.accountservice.features.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.accountservice.features.user.dto.params.AdminCreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.params.CreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;
import ru.simbir.health.accountservice.features.user.repositories.UserRepository;
import ru.simbir.health.accountservice.features.user.repositories.UserRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserEntity getById(long id) {
        return userRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getByUsername(String username) {
        return userRepository.findActiveByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getByUsernameWithRoles(String username) {
        return userRepository.findActiveByUsernameWithRoles(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<UserEntity> getAll(int offset, int limit) {
        return userRepository.findSliceOfActiveUsers(PageRequest.of(offset, limit));
    }

    @Override
    @Transactional
    public void update(long id, AdminCreateOrUpdateUserParams params) {
        final var user = getById(id);
        user.setLastName(params.lastName());
        user.setFirstName(params.firstName());
        user.setUsername(params.username());
        user.setPassword(params.password());
        update(user, true);

        updateRoles(user, params.roles());
    }

    @Override
    @Transactional
    public void update(long id, CreateOrUpdateUserParams params) {
        final var user = getById(id);
        user.setLastName(params.lastName());
        user.setFirstName(params.firstName());
        user.setPassword(params.password());
        update(user, true);
    }

    @Override
    @Transactional
    public void softDelete(long id) {
        var user = getById(id);
        user.setIsDeleted(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        if(userRepository.findActiveByUsername(user.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is busy");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserEntity create(AdminCreateOrUpdateUserParams params) {
        final var user = new UserEntity();
        user.setLastName(params.lastName());
        user.setFirstName(params.firstName());
        user.setUsername(params.username());
        user.setPassword(params.password());
        final var savedUser = create(user);
        updateRoles(savedUser, params.roles());
        return savedUser;
    }

    @Override
    @Transactional
    public void update(UserEntity user, boolean passwordEncoding) {
        if(userRepository.findActiveByUsername(user.getUsername(), user.getId()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is busy");

        if (passwordEncoding) user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    private void updateRoles(UserEntity user, List<String> roles) {
        if (roles != null && !roles.isEmpty()) {
            userRoleRepository.deleteAllByUserId(user.getId());

            List<UserRoleEntity> newRoles = roles.stream()
                    .map(roleName -> {
                        var userRoleEntity = new UserRoleEntity();
                        var userRoleId = new UserRoleEntityId();
                        userRoleId.setUserId(user.getId());
                        userRoleId.setRole(UserRoleEntityId.Role.valueOf(roleName));
                        userRoleEntity.setId(userRoleId);
                        return userRoleEntity;
                    })
                    .collect(Collectors.toList());

            userRoleRepository.saveAll(newRoles);
        }
    }
}
