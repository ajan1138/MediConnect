package dev.ahmedajan.mediconnect.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_POST("admin:post"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_PUT("admin:put"),
    ADMIN_DELETE("admin:delete"),

    DOCTOR_READ("doctor:read"),
    DOCTOR_POST("doctor:post"),
    DOCTOR_UPDATE("doctor:update"),
    DOCTOR_PUT("doctor:put"),
    DOCTOR_DELETE("doctor:delete"),

    PATIENT_READ("patient:read"),
    PATIENT_POST("patient:post"),
    PATIENT_UPDATE("patient:update"),
    PATIENT_PUT("patient:put"),
    PATIENT_DELETE("patient:delete")

    ;


    @Getter
    private final String permission;

}
