package io.github.rezi_gelenidze.gym_crm.main_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super("INVALID_CREDENTIALS", "Invalid credentials", HttpStatus.UNAUTHORIZED);
    }
}
