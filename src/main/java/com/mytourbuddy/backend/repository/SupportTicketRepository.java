package com.mytourbuddy.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytourbuddy.backend.model.SupportTicket;
import com.mytourbuddy.backend.model.SupportTicketStatus;

public interface SupportTicketRepository extends MongoRepository<SupportTicket, String> {
    List<SupportTicket> findByUserId(String userId);

    List<SupportTicket> findByStatus(SupportTicketStatus status);
}
