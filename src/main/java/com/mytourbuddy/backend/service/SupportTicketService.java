package com.mytourbuddy.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.CloseTicketRequest;
import com.mytourbuddy.backend.dto.request.SupportTicketRequest;
import com.mytourbuddy.backend.dto.response.SupportTicketResponse;
import com.mytourbuddy.backend.mapper.SupportTicketMapper;
import com.mytourbuddy.backend.model.SupportTicket;
import com.mytourbuddy.backend.model.SupportTicketStatus;
import com.mytourbuddy.backend.repository.SupportTicketRepository;
import com.mytourbuddy.backend.security.CustomUserDetails;
import com.mytourbuddy.backend.util.IdGenerator;

@Service
public class SupportTicketService {
    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private SupportTicketMapper supportTicketMapper;

    @Autowired
    private IdGenerator idGenerator;

    // create new ticket
    public SupportTicketResponse createTicket(SupportTicketRequest request) {
        String userId = getCurrentUserId();
        SupportTicket ticket = supportTicketMapper.toEntity(request, userId);

        String ticketId = idGenerator.generate("sup", supportTicketRepository::existsById);
        ticket.setId(ticketId);

        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        return supportTicketMapper.toResponse(savedTicket);
    }

    // get tickets by current user
    public List<SupportTicketResponse> getTicketsByUser() {
        String userId = getCurrentUserId();
        List<SupportTicket> tickets = supportTicketRepository.findByUserId(userId);
        return tickets.stream()
                .map(supportTicketMapper::toResponse)
                .collect(Collectors.toList());
    }

    // get all tickets (for admins)
    public List<SupportTicketResponse> getAllTickets() {
        List<SupportTicket> tickets = supportTicketRepository.findAll();
        return tickets.stream()
                .map(supportTicketMapper::toResponse)
                .collect(Collectors.toList());
    }

    public SupportTicketResponse getTicketById(String ticketId) {
        String currentUserId = getCurrentUserId();

        Optional<SupportTicket> ticketOpt = supportTicketRepository.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new IllegalArgumentException("Ticket not found");
        }

        SupportTicket ticket = ticketOpt.get();
        if (!ticket.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Access denied");
        }

        return supportTicketMapper.toResponse(ticket);
    }

    public SupportTicketResponse closeTicket(String ticketId, CloseTicketRequest request) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        if (ticket.getStatus() == SupportTicketStatus.CLOSED) {
            throw new IllegalArgumentException("Ticket is already closed");
        }

        String adminId = getCurrentUserId();
        supportTicketMapper.updateEntityForClose(request, ticket, adminId);
        SupportTicket updatedTicket = supportTicketRepository.save(ticket);
        return supportTicketMapper.toResponse(updatedTicket);
    }

    private String getCurrentUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUserId();
    }
}
