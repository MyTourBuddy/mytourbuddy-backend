package com.mytourbuddy.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytourbuddy.backend.model.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {
    
}
