package io.github.rezi_gelenidze.gym_crm.dto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String error;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fieldErrors;

    public ErrorResponse(String error, String message, Map<String, String> fieldErrors) {
        this.timestamp = LocalDateTime.now();  // Current date and time auto-generated
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public ErrorResponse(String error, String message) {
        this.timestamp = LocalDateTime.now();  // Current date and time auto-generated
        this.error = error;
        this.message = message;
        this.fieldErrors = null;
    }
}
