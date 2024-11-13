package ru.practicum.dto.mainservice.exception;

public class ConditionsAreNotMet extends RuntimeException {
    public ConditionsAreNotMet(String message) {
        super(message);
    }
}
