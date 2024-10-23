package ru.simbir.health.accountservice.features.user.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/Authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserSecurityService userSecurityService;

    @PostMapping("/SignUp")
    public JwtResponseDto signUp(
            @Valid @RequestBody SignUpParams params
    ) {
        return userSecurityService.signUp(params);
    }

    @PostMapping("/SignIn")
    public JwtResponseDto signIn(
            @Valid @RequestBody SignInParams params
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
    public JwtValidateResponseDto validateToken(
            @RequestParam String accessToken
    ) {
        return userSecurityService.validateToken(accessToken);
    }

    @PostMapping("/Refresh")
    public JwtResponseDto refreshToken(
            @Valid @RequestBody JwtRefreshParams params
    ) {
        return userSecurityService.refreshToken(params);
    }
}
