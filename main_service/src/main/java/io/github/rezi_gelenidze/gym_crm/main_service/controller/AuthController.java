package io.github.rezi_gelenidze.gym_crm.main_service.controller;

import io.github.rezi_gelenidze.gym_crm.main_service.dto.auth.ChangePasswordRequestDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.auth.CredentialsDto;
import io.github.rezi_gelenidze.gym_crm.main_service.dto.auth.JwtDto;
import io.github.rezi_gelenidze.gym_crm.main_service.exception.ApiException;
import io.github.rezi_gelenidze.gym_crm.main_service.exception.InvalidCredentialsException;
import io.github.rezi_gelenidze.gym_crm.main_service.service.JwtService;
import io.github.rezi_gelenidze.gym_crm.main_service.service.LoginAttemptService;
import io.github.rezi_gelenidze.gym_crm.main_service.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "user authentication and password change")
public class AuthController {

    private final UserService userService;
    private final LoginAttemptService loginAttemptService;
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates the user and returns a JWT token."
    )
    public ResponseEntity<JwtDto> login(@RequestBody @Valid CredentialsDto request) {
        if (loginAttemptService.isBlocked(request.getUsername()))
            // Throw ApiException with 403
            throw new ApiException(
                    "ATTEMPT_LIMIT_REACHED", "User is temporarily blocked due to multiple failed login attempts", HttpStatus.FORBIDDEN
            );

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            // record authentication failure
            loginAttemptService.loginFailed(request.getUsername());
            throw new InvalidCredentialsException();
        }

        // record success
        loginAttemptService.loginSucceeded(request.getUsername());

        // Authenticate and return token
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        return ResponseEntity.ok(new JwtDto(jwtService.generateToken(user)));
    }



    @PutMapping("/change-password")
    @Operation(
            summary = "Change Password",
            description = "Allows the user to change their password after authentication."
    )
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequestDto request) {
        userService.updatePassword(request.getUsername(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
