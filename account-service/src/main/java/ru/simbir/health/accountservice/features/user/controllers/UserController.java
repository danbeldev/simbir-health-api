package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.accountservice.features.security.dto.JwtResponseDto;
import ru.simbir.health.accountservice.features.security.dto.JwtValidateResponseDto;
import ru.simbir.health.accountservice.features.security.dto.params.JwtRefreshParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignInParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignUpParams;
import ru.simbir.health.accountservice.features.security.jwt.JwtUserDetails;
import ru.simbir.health.accountservice.features.security.services.UserSecurityService;
import ru.simbir.health.accountservice.features.user.dto.params.AdminCreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.params.CreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.UserEntityDto;
import ru.simbir.health.accountservice.features.user.entities.role.UserRoleEntityId;
import ru.simbir.health.accountservice.features.user.mappers.UserEntityMapper;
import ru.simbir.health.accountservice.features.user.services.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;

    private final UserEntityMapper userEntityMapper;

    @PostMapping("/Authentication/SignUp")
    public JwtResponseDto signUp(
            @RequestBody SignUpParams params
    ) {
        return userSecurityService.signUp(params);
    }

    @PostMapping("/Authentication/SignIn")
    public JwtResponseDto signIn(
            @RequestBody SignInParams params
    ) {
        return userSecurityService.signIn(params);
    }

    @PutMapping("/Authentication/SignOut")
    @SecurityRequirement(name = "bearerAuth")
    public void signOut(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        userSecurityService.signOut(jwtUserDetails.getTokenId());
    }

    @GetMapping("/Authentication/Validate")
    public JwtValidateResponseDto validateToken(
            @RequestParam String accessToken
    ) {
        return userSecurityService.validateToken(accessToken);
    }

    @PostMapping("/Authentication/Refresh")
    public JwtResponseDto refreshToken(
            @RequestBody JwtRefreshParams params
    ) {
        return userSecurityService.refreshToken(params);
    }

    @GetMapping("/Accounts/Me")
    @SecurityRequirement(name = "bearerAuth")
    public UserEntityDto getInfo(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        return userEntityMapper.toDto(userService.getById(jwtUserDetails.getUserId()));
    }

    @PutMapping("/Accounts/Update")
    @SecurityRequirement(name = "bearerAuth")
    public void update(
            @RequestBody CreateOrUpdateUserParams params,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        userService.update(jwtUserDetails.getUserId(), params);
    }

    @GetMapping("/Accounts")
    @SecurityRequirement(name = "bearerAuth")
    public List<UserEntityDto> getAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int count
    ) {
        return userService.getAll(from, count).getContent().stream().map(userEntityMapper::toDto).toList();
    }

    @PostMapping("/Accounts")
    @SecurityRequirement(name = "bearerAuth")
    public UserEntityDto create(
            @RequestBody AdminCreateOrUpdateUserParams params
    ) {
        return userEntityMapper.toDto(userService.create(params));
    }

    @PutMapping("/Accounts/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void update(
            @PathVariable long id,
            @RequestBody AdminCreateOrUpdateUserParams params
    ) {
        userService.update(id, params);
    }

    @DeleteMapping("/Accounts/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void delete(
            @PathVariable long id
    ) {
        userService.softDelete(id);
    }

    @Hidden
    @GetMapping("/Accounts/{id}/Is-Exists")
    public boolean isExists(
            @PathVariable long id,
            @RequestParam Collection<UserRoleEntityId.Role> roles,
            @RequestParam(required = false, defaultValue = "false") boolean requireAll
    ) {
        return userService.isExists(id, roles, requireAll);
    }

    @GetMapping("/Doctors")
    @SecurityRequirement(name = "bearerAuth")
    public List<UserEntityDto> getAllDoctors(
            @RequestParam(required = false) String nameFilter,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int count
    ) {
        return userService.getAll(from, count, nameFilter, UserRoleEntityId.Role.Doctor)
                .getContent().stream().map(userEntityMapper::toDto).toList();
    }

    @GetMapping("/Doctors/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public UserEntityDto getDoctorById(
            @PathVariable long id
    ) {
        return userEntityMapper.toDto(userService.getById(id, UserRoleEntityId.Role.Doctor));
    }
}
