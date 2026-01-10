package com.mytourbuddy.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookingStatus {
    PENDING, CONFIRMED, COMPLETED, CANCELLED;

    @JsonCreator
    public static BookingStatus fromString(String value) {
        return BookingStatus.valueOf(value.toUpperCase());
    }
}