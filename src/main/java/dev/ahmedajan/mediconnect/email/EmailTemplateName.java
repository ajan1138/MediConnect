package dev.ahmedajan.mediconnect.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    PATIENT_ACTIVATE_ACCOUNT("activate_account"),
    DOCTOR_ACTIVATE_ACCOUNT("doctor_activate_account"),
    DOCTOR_PENDING_APPROVAL("doctor_pending_approval");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
