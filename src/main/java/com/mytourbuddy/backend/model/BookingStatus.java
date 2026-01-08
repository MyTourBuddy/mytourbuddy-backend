package com.mytourbuddy.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookingStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED,
    COMPLETED;

    @JsonCreator
    public static BookingStatus fromString(String value) {
        return BookingStatus.valueOf(value.toUpperCase());
    }
}