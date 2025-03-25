package io.github.rezi_gelenidze.gym_crm.exception;

import io.github.rezi_gelenidze.gym_crm.dto.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle API exceptions (Custom exception handling)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, WebRequest request) {
        logger.warn("API Exception: {} - {}", ex.getError(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                ex.getError(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Handle validation errors (e.g., @Valid validation failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        logger.warn("Validation failed: {}", errors);

        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                "Validation Error",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle invalid JSON structure (e.g., missing commas, malformed input)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {
        logger.error("Invalid JSON request: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "BAD_REQUEST",
                "Malformed JSON request"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle invalid endpoints (404 Not Found)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException ex, WebRequest request) {
        logger.error("No handler found for request: {}", ex.getResourcePath());

        ErrorResponse response = new ErrorResponse(
                "NOT_FOUND",
                "Endpoint not found"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle method not allowed (405 Method Not Allowed)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        logger.error("Method Not Allowed: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "METHOD_NOT_ALLOWED",
                "Invalid request method"
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    // Handle missing parameters (e.g., missing query params)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        logger.error("Missing request parameter: {}", ex.getParameterName());

        ErrorResponse response = new ErrorResponse(
                "BAD_REQUEST",
                "Missing required parameter: " + ex.getParameterName()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle all other unexpected errors (Internal Server Error - 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred: ", ex);

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
