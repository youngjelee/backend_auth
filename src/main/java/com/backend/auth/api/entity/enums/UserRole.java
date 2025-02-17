package com.backend.auth.api.entity.enums;

public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}