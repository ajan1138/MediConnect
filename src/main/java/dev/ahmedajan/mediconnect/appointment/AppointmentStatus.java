package dev.ahmedajan.mediconnect.appointment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AppointmentStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    NO_SHOW("No Show");

    @Getter
    private final String message;

    public static AppointmentStatus fromMessage(String message) {
        for (AppointmentStatus status : values()) {
            if (status.getMessage().equalsIgnoreCase(message)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No AppointmentStatus with message: " + message);
    }
}
