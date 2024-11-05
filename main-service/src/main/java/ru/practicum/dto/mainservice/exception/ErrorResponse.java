package ru.practicum.dto.mainservice.exception;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {

    private String status;
    private String reason;
    private String message;
    private Instant timestamp;

    public ErrorResponse(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = Instant.now();
    }
}
