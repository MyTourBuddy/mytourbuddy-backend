package com.mytourbuddy.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    TOURIST,
    GUIDE,
    ADMIN;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
