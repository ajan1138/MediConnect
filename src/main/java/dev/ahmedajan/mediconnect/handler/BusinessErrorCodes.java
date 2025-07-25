package dev.ahmedajan.mediconnect.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "New password doesnt match"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled!"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Email and / or password is incorrect"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatusCode;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatus;
    }
}
