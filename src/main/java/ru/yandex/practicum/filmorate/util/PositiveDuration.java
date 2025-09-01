package ru.yandex.practicum.filmorate.util;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveDurationValidator.class)
public @interface PositiveDuration {
    String message() default "Длительность должна быть положительной";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
