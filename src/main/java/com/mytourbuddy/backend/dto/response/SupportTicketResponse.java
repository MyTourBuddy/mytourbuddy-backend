package com.mytourbuddy.backend.dto.response;

import java.time.Instant;

import com.mytourbuddy.backend.model.SupportTicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketResponse {
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
