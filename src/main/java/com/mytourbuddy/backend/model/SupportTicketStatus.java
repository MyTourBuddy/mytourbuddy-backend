package com.mytourbuddy.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SupportTicketStatus {
    OPEN,
    CLOSED;

    @JsonCreator
    public static SupportTicketStatus fromString(String value) {
        return SupportTicketStatus.valueOf(value.toUpperCase());
    }
}
