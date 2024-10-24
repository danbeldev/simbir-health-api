package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.health.accountservice.features.security.dto.JwtResponseDto;
import ru.simbir.health.accountservice.features.security.dto.JwtValidateResponseDto;
import ru.simbir.health.accountservice.features.security.dto.params.JwtRefreshParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignInParams;
import ru.simbir.health.accountservice.features.security.dto.params.SignUpParams;
import ru.simbir.health.accountservice.features.security.jwt.JwtUserDetails;
import ru.simbir.health.accountservice.features.security.services.UserSecurityService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Authentication")
@Tag(name = "Аутентификация", description = "Эндпоинты для управления пользователями и аутентификации.")
public class AuthenticationController {

    private final UserSecurityService userSecurityService;

    @PostMapping("/SignUp")
    @Operation(summary = "Регистрация нового пользователя", description = "Создаёт новую учётную запись пользователя с предоставленными данными.")
    public JwtResponseDto signUp(
            @Valid @RequestBody SignUpParams params
    ) {
        return userSecurityService.signUp(params);
    }

    @PostMapping("/SignIn")
    @Operation(summary = "Вход пользователя", description = "Аутентифицирует пользователя и возвращает JWT токен.")
    public JwtResponseDto signIn(
            @Valid @RequestBody SignInParams params
    ) {
        return userSecurityService.signIn(params);
    }

    @PutMapping("/SignOut")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Выход пользователя", description = "Выводит пользователя из системы и аннулирует JWT токен.")
    public void signOut(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        userSecurityService.signOut(jwtUserDetails.getTokenId());
    }

    @GetMapping("/Validate")
    @Operation(summary = "Проверка JWT токена", description = "Проверяет, действителен ли предоставленный токен доступа.")
    public JwtValidateResponseDto validateToken(
            @RequestParam String accessToken
    ) {
        return userSecurityService.validateToken(accessToken);
    }

    @PostMapping("/Refresh")
    @Operation(summary = "Обновление JWT токена", description = "Генерирует новый JWT токен с использованием токена обновления.")
    public JwtResponseDto refreshToken(
            @Valid @RequestBody JwtRefreshParams params
    ) {
        return userSecurityService.refreshToken(params);
    }
}