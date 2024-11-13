package ru.practicum.dto.mainservice.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<StartAfterTwoHours, Object> {

    private String dateMethod;

    @Override
    public void initialize(StartAfterTwoHours constraintAnnotation) {
        this.dateMethod = constraintAnnotation.dateMethod();
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        try {
            Method method = dto.getClass().getMethod(dateMethod);
            LocalDateTime start = (LocalDateTime) method.invoke(dto);
            return start != null && start.isAfter(LocalDateTime.now().plusHours(2));
        } catch (Exception e) {
            return false;
        }
    }
}