package com.qard.QardHasanah.exception;

import com.qard.QardHasanah.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                "Validation failed",
                errors,
                false,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                ex.getMessage(),
                null,
                false,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "A database constraint was violated.";

        // Check for duplicate email error
        String rootCauseMessage = ex.getMostSpecificCause().getMessage();
        if (rootCauseMessage != null) {
            if (rootCauseMessage.contains("email") || rootCauseMessage.contains("uk6dotkott2kjsp8vw4d0m25fb7")) {
                message = "This email is already registered. Please use a different email or login to your existing account.";
            } else if (rootCauseMessage.contains("duplicate key")) {
                message = "This record already exists.";
            }
        }

        ApiResponse<String> response = new ApiResponse<>(
                message,
                null,
                false,
                HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex, WebRequest request) {
        String requestPath = request.getDescription(false).replace("uri=", "");

        // Don't wrap Swagger/OpenAPI endpoints
        if (requestPath.contains("/v3/api-docs") || requestPath.contains("/swagger") ||
            requestPath.contains("/webjars") || requestPath.contains("/favicon")) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ApiResponse<String> response = new ApiResponse<>(
                "An error occurred: " + ex.getMessage(),
                null,
                false,
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

