package com.mytourbuddy.backend.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.mytourbuddy.backend.dto.request.CloseTicketRequest;
import com.mytourbuddy.backend.dto.request.SupportTicketRequest;
import com.mytourbuddy.backend.dto.response.SupportTicketResponse;
import com.mytourbuddy.backend.model.SupportTicket;
import com.mytourbuddy.backend.model.SupportTicketStatus;

@Component
public class SupportTicketMapper {
    public SupportTicket toEntity(SupportTicketRequest request, String userId) {
        SupportTicket ticket = new SupportTicket();

        ticket.setUserId(userId);
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(SupportTicketStatus.OPEN);
        ticket.setCreatedAt(Instant.now());
        ticket.setUpdatedAt(Instant.now());
        ticket.setAdminResponse(null);
        ticket.setClosedByAdminId(null);

        return ticket;
    }

    public void updateEntityForClose(CloseTicketRequest request, SupportTicket ticket, String adminId) {
        ticket.setAdminResponse(request.getAdminResponse());
        ticket.setClosedByAdminId(adminId);
        ticket.setStatus(SupportTicketStatus.CLOSED);
        ticket.setUpdatedAt(Instant.now());
    }

    public SupportTicketResponse toResponse(SupportTicket ticket) {
        return new SupportTicketResponse(
                ticket.getId(),
                ticket.getUserId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getClosedByAdminId(),
                ticket.getAdminResponse());
    }
}
