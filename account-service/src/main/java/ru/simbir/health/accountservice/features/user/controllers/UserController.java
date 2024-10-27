package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.accountservice.common.validate.pagination.PaginationLimit;
import ru.simbir.health.accountservice.common.validate.pagination.PaginationOffset;
import ru.simbir.health.accountservice.common.validate.pagination.ValidPagination;
import ru.simbir.health.accountservice.features.security.jwt.JwtUserDetails;
import ru.simbir.health.accountservice.features.user.dto.params.AdminCreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.params.CreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.UserEntityDto;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;
import ru.simbir.health.accountservice.features.user.mappers.UserEntityMapper;
import ru.simbir.health.accountservice.features.user.services.UserService;

import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/Accounts")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Эндпоинты для управления пользователями.")
public class UserController {

    private final UserService userService;

    private final UserEntityMapper userEntityMapper;

    @GetMapping("/Me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Получить информацию о текущем пользователе", description = "Возвращает информацию о пользователе, который выполнил вход.")
    public UserEntityDto getInfo(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        return userEntityMapper.toDto(userService.getById(jwtUserDetails.getUserId()));
    }

    @PutMapping("/Update")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Обновить информацию о текущем пользователе", description = "Обновляет данные текущего пользователя.")
    public void update(
            @Valid @RequestBody CreateOrUpdateUserParams params,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        userService.update(jwtUserDetails.getUserId(), params);
    }

    @GetMapping
    @ValidPagination
    @PreAuthorize("hasRole('Admin')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей с возможностью пагинации.")
    public List<UserEntityDto> getAll(
            @PaginationOffset
            @RequestParam(defaultValue = "0")
            int from,

            @PaginationLimit
            @RequestParam(defaultValue = "20")
            int count
    ) {
        return userService.getAll(from, count).getContent().stream().map(userEntityMapper::toDto).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Создать нового пользователя", description = "Создаёт нового пользователя с заданными параметрами.")
    public UserEntityDto create(
            @Valid @RequestBody AdminCreateOrUpdateUserParams params
    ) {
        return userEntityMapper.toDto(userService.create(params));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Обновить пользователя по ID", description = "Обновляет информацию о пользователе с указанным идентификатором.")
    public void update(
            @PathVariable long id,
            @Valid @RequestBody AdminCreateOrUpdateUserParams params
    ) {
        userService.update(id, params);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя с указанным идентификатором.")
    public void delete(
            @PathVariable long id
    ) {
        userService.softDelete(id);
    }

    @Hidden
    @GetMapping("/{id}/Is-Exists")
    @Operation(summary = "Проверить существование пользователя", description = "Проверяет, существует ли пользователь с указанным ID и ролями.")
    public boolean isExists(
            @PathVariable long id,
            @RequestParam Collection<UserRoleEntityId.Role> roles,
            @RequestParam(required = false, defaultValue = "false") boolean requireAll
    ) {
        return userService.isExists(id, roles, requireAll);
    }
}