package com.runapp.guildservice.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private Map<String, String> errors;

    // Constructors, getters, and setters

    public ValidationErrorResponse(LocalDateTime timestamp, String message, Map<String, String> errors) {
        this.timestamp = timestamp;
        this.message = message;
        this.errors = errors;
    }

    // Other constructors, getters, and setters as needed
}
