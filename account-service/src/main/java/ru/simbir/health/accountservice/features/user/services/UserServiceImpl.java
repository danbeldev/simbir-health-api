package ru.simbir.health.accountservice.features.user.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.accountservice.common.message.LocalizedMessageService;
import ru.simbir.health.accountservice.features.timetable.client.TimetableServiceClient;
import ru.simbir.health.accountservice.features.user.dto.params.AdminCreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.params.CreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.entities.UserEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntity;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;
import ru.simbir.health.accountservice.features.user.repositories.UserRepository;
import ru.simbir.health.accountservice.features.user.repositories.UserRoleRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public final UserRoleRepository userRoleRepository;

    private final LocalizedMessageService localizedMessageService;

    private final PasswordEncoder passwordEncoder;

    private final TimetableServiceClient timetableServiceClient;

    @Override
    @Transactional(readOnly = true)
    public UserEntity getById(long id) {
        return userRepository.findActiveById(id)
                .orElseThrow(() -> {
                    var message = localizedMessageService.getMessage("user.notfound.id", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getById(long id, UserRoleEntityId.Role role) {
        var user = getById(id);
        if (user.getRoles().stream().noneMatch(r -> r.getId().getRole().equals(role))) {
            var message = localizedMessageService.getMessage("user.notfound.id", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getByUsername(String username) {
        return userRepository.findActiveByUsername(username)
                .orElseThrow(() -> {
                    var message = localizedMessageService.getMessage("user.notfound.username", username);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getByUsernameWithRoles(String username) {
        return userRepository.findActiveByUsernameWithRoles(username)
                .orElseThrow(() -> {
                    var message = localizedMessageService.getMessage("user.notfound.username", username);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<UserEntity> getAll(int offset, int limit) {
        return userRepository.findSliceOfActiveUsers(PageRequest.of(offset, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<UserEntity> getAll(int offset, int limit, String nameFilter, UserRoleEntityId.Role role) {
        List<UserEntity> users;

        if (nameFilter != null && !nameFilter.isBlank())
            users = userRepository.findAllActiveByFullName(nameFilter);
        else
            users = userRepository.findAllActiveUsers();

        List<UserEntity> usersByRole = users.stream()
                .filter(user -> user.getRoles().stream().anyMatch(r -> r.getId().getRole().equals(role)))
                .toList();

        int start = Math.toIntExact((long) offset * limit);

        if (start >= usersByRole.size()) {
            return new SliceImpl<>(Collections.emptyList(), PageRequest.of(offset, limit), false);
        }

        int end = Math.min((start + limit), usersByRole.size());
        List<UserEntity> pagedUsers = usersByRole.subList(start, end);

        boolean hasNext = end < usersByRole.size();

        return new SliceImpl<>(pagedUsers, PageRequest.of(offset, limit), hasNext);
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
    public void softDelete(long id, String accessToken) {
        var user = getById(id);
        user.setIsDeleted(true);
        userRepository.save(user);

        if (user.getRoles().stream().anyMatch(r -> r.getId().getRole() == UserRoleEntityId.Role.Doctor)) {
            try {
                timetableServiceClient.deleteByHospitalId(id, accessToken);
            }catch (FeignException ex) {
                if (ex.status() == -1) {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
                }else {
                    throw ex;
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = ResponseStatusException.class)
    public boolean isExists(long id, Collection<UserRoleEntityId.Role> roles, boolean requireAll) {
        UserEntity user;
        try {
            user = getById(id);
        } catch (ResponseStatusException e) {
            return false;
        }

        Set<UserRoleEntityId.Role> userRoles = user.getRoles().stream()
                .map(r -> r.getId().getRole())
                .collect(Collectors.toSet());

        if (requireAll) {
            return userRoles.containsAll(roles);
        } else {
            return roles.stream().anyMatch(userRoles::contains);
        }
    }

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        if (userRepository.findActiveByUsername(user.getUsername()).isPresent()) {
            var message = localizedMessageService.getMessage("user.username.busy");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

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
        if (userRepository.findActiveByUsername(user.getUsername(), user.getId()).isPresent()) {
            var message = localizedMessageService.getMessage("user.username.busy");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        if (passwordEncoding) user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateRoles(UserEntity user, List<UserRoleEntityId.Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            Set<UserRoleEntityId.Role> existingRoleNames = user.getRoles().stream()
                    .map(role -> role.getId().getRole())
                    .collect(Collectors.toSet());

            List<UserRoleEntity> rolesToAdd = roles.stream()
                    .filter(roleName -> !existingRoleNames.contains(roleName))
                    .map(roleName -> {
                        var userRoleEntity = new UserRoleEntity();
                        var userRoleId = new UserRoleEntityId();
                        userRoleId.setUser(user);
                        userRoleId.setRole(roleName);
                        userRoleEntity.setId(userRoleId);
                        return userRoleEntity;
                    }).collect(Collectors.toList());

            List<UserRoleEntity> rolesToRemove = user.getRoles().stream()
                    .filter(role -> !roles.contains(role.getId().getRole()))
                    .collect(Collectors.toList());

            if (!rolesToAdd.isEmpty()) {
                userRoleRepository.saveAll(rolesToAdd);
            }

            if (!rolesToRemove.isEmpty()) {
                userRoleRepository.deleteAll(rolesToRemove);
            }
        }
    }
}
