package ru.practicum.service.exception;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {

    private String message;
    private Instant timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }
}
