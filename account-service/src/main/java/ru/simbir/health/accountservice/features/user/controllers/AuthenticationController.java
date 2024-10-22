package ru.simbir.health.accountservice.features.user.controllers;

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

@RestController
@RequestMapping("/api/Authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserSecurityService userSecurityService;

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
}
