package ru.practicum.service.exception;

public class IncorrectDateParam extends RuntimeException {
    public IncorrectDateParam(String message) {
        super(message);
    }
}
