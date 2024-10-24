package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/Doctors")
public class DoctorsController {

    private final UserService userService;

    private final UserEntityMapper userEntityMapper;

    @GetMapping
    @ValidPagination
    @SecurityRequirement(name = "bearerAuth")
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
    public UserEntityDto getDoctorById(
            @PathVariable long id
    ) {
        return userEntityMapper.toDto(userService.getById(id, UserRoleEntityId.Role.Doctor));
    }
}