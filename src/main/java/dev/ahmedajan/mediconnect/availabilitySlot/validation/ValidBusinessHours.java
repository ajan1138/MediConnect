package dev.ahmedajan.mediconnect.availabilitySlot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusinessHoursValidator.class)
public @interface ValidBusinessHours {
    String message() default "Invalid business hours";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}