package com.mytourbuddy.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.dto.request.CloseTicketRequest;
import com.mytourbuddy.backend.dto.request.SupportTicketRequest;
import com.mytourbuddy.backend.dto.response.SupportTicketResponse;
import com.mytourbuddy.backend.service.SupportTicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tickets")
public class SupportTicketContoller {
    @Autowired
    private SupportTicketService supportTicketService;

    // create ticket
    @PostMapping
    public ResponseEntity<SupportTicketResponse> createTicket(@Valid @RequestBody SupportTicketRequest request) {
        SupportTicketResponse response = supportTicketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // get tickets for current user
    @GetMapping
    public ResponseEntity<List<SupportTicketResponse>> getTickets() {
        List<SupportTicketResponse> responses = supportTicketService.getTicketsByUser();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponse> getTickets(@PathVariable String id) {
        SupportTicketResponse response = supportTicketService.getTicketById(id);
        return ResponseEntity.ok(response);
    }

    // get all tickets (for admins)
    @GetMapping("/all")
    public ResponseEntity<List<SupportTicketResponse>> getAllTickets() {
        List<SupportTicketResponse> responses = supportTicketService.getAllTickets();
        return ResponseEntity.ok(responses);
    }

    // close ticket (for admins)
    @PutMapping("/{id}/close")
    public ResponseEntity<SupportTicketResponse> closeTicket(@PathVariable String id,
            @Valid @RequestBody CloseTicketRequest request) {
        SupportTicketResponse response = supportTicketService.closeTicket(id, request);
        return ResponseEntity.ok(response);
    }
}
