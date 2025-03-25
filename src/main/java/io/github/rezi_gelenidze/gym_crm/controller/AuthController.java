package io.github.rezi_gelenidze.gym_crm.controller;

import io.github.rezi_gelenidze.gym_crm.dto.auth.ChangePasswordRequestDto;
import io.github.rezi_gelenidze.gym_crm.dto.auth.CredentialsDto;
import io.github.rezi_gelenidze.gym_crm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "user authentication and password change")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates the user with given credentials."
    )
    public ResponseEntity<Void> login(@RequestBody @Valid CredentialsDto request) {
        userService.authenticate(request);

        // Endpoint is redundant, but we will add token auth
        // in Security Chapter later I hope :)
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    @Operation(
            summary = "Change Password",
            description = "Allows the user to change their password after authentication."
    )
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequestDto request) {
        userService.authenticate(new CredentialsDto(request.getUsername(), request.getPassword()));
        userService.updatePassword(request.getUsername(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
