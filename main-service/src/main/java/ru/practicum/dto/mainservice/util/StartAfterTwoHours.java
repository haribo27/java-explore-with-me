package ru.practicum.dto.mainservice.util;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CheckDateValidator.class})
public @interface StartAfterTwoHours {
    String message() default "Дата должна быть не ранее, чем через 2 часа";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String dateMethod();
}