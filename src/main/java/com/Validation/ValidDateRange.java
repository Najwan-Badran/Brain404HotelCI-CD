package com.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for date range validation.
 * Ensures that the start date is before the end date.
 */
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {

    String message() default "Check-out date must be after check-in date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startDateField() default "checkInDate";

    String endDateField() default "checkOutDate";
}
