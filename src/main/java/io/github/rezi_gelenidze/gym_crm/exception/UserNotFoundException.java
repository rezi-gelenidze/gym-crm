package io.github.rezi_gelenidze.gym_crm.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException() {
        super("USER_NOT_FOUND", "User not found.", HttpStatus.NOT_FOUND);
    }
}
