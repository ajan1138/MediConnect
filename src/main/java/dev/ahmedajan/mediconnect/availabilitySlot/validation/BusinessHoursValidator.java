package dev.ahmedajan.mediconnect.availabilitySlot.validation;

import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BusinessHoursValidator implements ConstraintValidator<ValidBusinessHours, ReservedSlotTime> {

    private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);
    private static final int MIN_DURATION_MINUTES = 15;
    private static final int MAX_DURATION_MINUTES = 120;

    @Override
    public boolean isValid(ReservedSlotTime slot, ConstraintValidatorContext context) {
        boolean isValid = true;

        // 1. Chronological check
        if (slot.getStartTime().isAfter(slot.getEndTime())) {
            addViolation(context, "Start time must be before end time", "startTime");
            isValid = false;
        }

        // 2. Weekday check
        if (isWeekend(slot.getStartTime())) {
            addViolation(context, "Slots not allowed on weekends", "startTime");
            isValid = false;
        }

        // 3. Business hours check
        if (!isWithinBusinessHours(slot)) {
            addViolation(context, "Slots must be between 9AM-5PM", "endTime");
            isValid = false;
        }

        // 4. Duration check (only if times are valid)
        if (isValid && !isValidDuration(slot)) {
            addViolation(context, "Slot duration must be 15-120 minutes", "endTime");
            isValid = false;
        }

        return isValid;
    }

    private boolean isWeekend(LocalDateTime time) {
        DayOfWeek day = time.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private boolean isWithinBusinessHours(ReservedSlotTime slot) {
        LocalTime start = slot.getStartTime().toLocalTime();
        LocalTime end = slot.getEndTime().toLocalTime();
        return !start.isBefore(BUSINESS_START) && !end.isAfter(BUSINESS_END);
    }

    private boolean isValidDuration(ReservedSlotTime slot) {
        long minutes = Duration.between(slot.getStartTime(), slot.getEndTime()).toMinutes();
        return minutes >= MIN_DURATION_MINUTES && minutes <= MAX_DURATION_MINUTES;
    }

    private void addViolation(ConstraintValidatorContext context, String message, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}