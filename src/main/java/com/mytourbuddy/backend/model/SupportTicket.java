package com.mytourbuddy.backend.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tickets")
public class SupportTicket {
    @Id
    private String id;

    private String userId;
    private String subject;
    private String description;

    private SupportTicketStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    private String closedByAdminId;
    private String adminResponse;
}
