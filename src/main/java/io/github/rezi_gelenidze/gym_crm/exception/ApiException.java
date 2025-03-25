package io.github.rezi_gelenidze.gym_crm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String error;

    public ApiException(String error, String message, HttpStatus status) {
        super(message);
        this.error = error;
        this.status = status;
    }
}
