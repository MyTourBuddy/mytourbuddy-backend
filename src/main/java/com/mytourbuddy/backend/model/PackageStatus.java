package com.mytourbuddy.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PackageStatus {
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static PackageStatus fromString(String value) {
        return PackageStatus.valueOf(value.toUpperCase());
    }
}
