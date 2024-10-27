package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.accountservice.common.validate.pagination.PaginationLimit;
import ru.simbir.health.accountservice.common.validate.pagination.PaginationOffset;
import ru.simbir.health.accountservice.common.validate.pagination.ValidPagination;
import ru.simbir.health.accountservice.features.user.dto.UserEntityDto;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;
import ru.simbir.health.accountservice.features.user.mappers.UserEntityMapper;
import ru.simbir.health.accountservice.features.user.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Doctors")
@Tag(name = "Доктора", description = "Эндпоинты для получения информации о врачах.")
public class DoctorsController {

    private final UserService userService;

    private final UserEntityMapper userEntityMapper;

    @GetMapping
    @ValidPagination
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Получить всех врачей", description = "Возвращает список всех врачей с возможностью фильтрации и пагинации.")
    public List<UserEntityDto> getAllDoctors(

            @RequestParam(required = false)
            String nameFilter,

            @PaginationOffset
            @RequestParam(defaultValue = "0")
            int from,

            @PaginationLimit
            @RequestParam(defaultValue = "20")
            int count
    ) {
        return userService.getAll(from, count, nameFilter, UserRoleEntityId.Role.Doctor)
                .getContent().stream().map(userEntityMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Получить врача по ID", description = "Возвращает информацию о враче по указанному идентификатору.")
    public UserEntityDto getDoctorById(
            @PathVariable long id
    ) {
        return userEntityMapper.toDto(userService.getById(id, UserRoleEntityId.Role.Doctor));
    }
}