package com.booknetwork.api.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    RESET_PASSWORD("reset_password"),
    CONFIRM_EMAIL("confirm_email");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
