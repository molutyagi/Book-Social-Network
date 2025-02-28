package com.booknetwork.api.exceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code."),
    INCORRECT_CURRENT_PASSWORDS(300, BAD_REQUEST, "Current Password is incorrect."),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new Password does not match."),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User Account is locked."),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User Account is disabled."),
    BAD_CREDENTIALS(304, FORBIDDEN, "Username and / or password are incorrect."),

    ;

    @Getter
    private final int code;
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final String description;

    private BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

}
