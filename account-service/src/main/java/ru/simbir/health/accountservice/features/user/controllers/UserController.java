package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class UserController {

    private final UserService userService;

    private final UserEntityMapper userEntityMapper;

    @GetMapping("/Me")
    @SecurityRequirement(name = "bearerAuth")
    public UserEntityDto getInfo(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        return userEntityMapper.toDto(userService.getById(jwtUserDetails.getUserId()));
    }

    @PutMapping("/Update")
    @SecurityRequirement(name = "bearerAuth")
    public void update(
            @Valid @RequestBody CreateOrUpdateUserParams params,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        userService.update(jwtUserDetails.getUserId(), params);
    }

    @GetMapping
    @ValidPagination
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
    public UserEntityDto create(
            @Valid @RequestBody AdminCreateOrUpdateUserParams params
    ) {
        return userEntityMapper.toDto(userService.create(params));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void update(
            @PathVariable long id,
            @Valid @RequestBody AdminCreateOrUpdateUserParams params
    ) {
        userService.update(id, params);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void delete(
            @PathVariable long id
    ) {
        userService.softDelete(id);
    }

    @Hidden
    @GetMapping("/{id}/Is-Exists")
    public boolean isExists(
            @PathVariable long id,
            @RequestParam Collection<UserRoleEntityId.Role> roles,
            @RequestParam(required = false, defaultValue = "false") boolean requireAll
    ) {
        return userService.isExists(id, roles, requireAll);
    }
}
