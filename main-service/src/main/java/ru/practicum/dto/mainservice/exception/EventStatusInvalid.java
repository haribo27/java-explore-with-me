package ru.practicum.dto.mainservice.exception;

public class EventStatusInvalid extends RuntimeException {
    public EventStatusInvalid(String message) {
        super(message);
    }
}
