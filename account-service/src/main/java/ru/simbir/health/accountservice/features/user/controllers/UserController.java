package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.accountservice.features.security.dto.JwtResponseDto;
import ru.simbir.health.accountservice.features.security.dto.params.JwtRefreshParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignInParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignUpParams;
import ru.simbir.health.accountservice.features.security.jwt.JwtUserDetails;
import ru.simbir.health.accountservice.features.security.services.UserSecurityService;
import ru.simbir.health.accountservice.features.user.dto.params.AdminCreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.params.CreateOrUpdateUserParams;
import ru.simbir.health.accountservice.features.user.dto.UserEntityDto;
import ru.simbir.health.accountservice.features.user.mappers.UserEntityMapper;
import ru.simbir.health.accountservice.features.user.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/Authentication")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;
    private final UserEntityMapper userEntityMapper;

    @PostMapping("/SignUp")
    public JwtResponseDto signUp(
            @RequestBody SignUpParams params
    ) {
        return userSecurityService.signUp(params);
    }

    @PostMapping("/SignIn")
    public JwtResponseDto signIn(
            @RequestBody SignInParams params
    ) {
        return userSecurityService.signIn(params);
    }

    @PutMapping("/SignOut")
    @SecurityRequirement(name = "bearerAuth")
    public void signOut(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        userSecurityService.signOut(jwtUserDetails.getTokenId());
    }

    @GetMapping("/Validate")
    public boolean validateToken(
            @RequestParam String accessToken
    ) {
        return userSecurityService.validateToken(accessToken);
    }

    @PostMapping("/Refresh")
    public JwtResponseDto refreshToken(
            @RequestBody JwtRefreshParams params
    ) {
        return userSecurityService.refreshToken(params);
    }

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
            @RequestBody CreateOrUpdateUserParams params,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        userService.update(jwtUserDetails.getUserId(), params);
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public List<UserEntityDto> getAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int count
    ) {
        return userService.getAll(from, count).getContent().stream().map(userEntityMapper::toDto).toList();
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public UserEntityDto create(
            @RequestBody AdminCreateOrUpdateUserParams params
    ) {
        return userEntityMapper.toDto(userService.create(params));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void update(
            @PathVariable long id,
            @RequestBody AdminCreateOrUpdateUserParams params
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
}
