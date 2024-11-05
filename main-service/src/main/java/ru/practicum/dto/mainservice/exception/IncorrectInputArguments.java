package ru.practicum.dto.mainservice.exception;

public class IncorrectInputArguments extends RuntimeException {
    public IncorrectInputArguments(String message) {
        super(message);
    }
}
