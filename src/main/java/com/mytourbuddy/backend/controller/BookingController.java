package com.mytourbuddy.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.dto.request.BookingRequest;
import com.mytourbuddy.backend.dto.request.BookingUpdateRequest;
import com.mytourbuddy.backend.dto.response.BookingResponse;
import com.mytourbuddy.backend.model.Booking;
import com.mytourbuddy.backend.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        BookingResponse created = bookingService.createBooking(request, userId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getTouristsBookings() {
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        List<Booking> bookings = bookingService.getTouristBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable String id) {
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Booking booking = bookingService.getBookingById(id, userId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable String id,
            @Valid @RequestBody BookingUpdateRequest request) {
        String guideId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        BookingResponse updated = bookingService.updateBooking(id, request, guideId);
        return ResponseEntity.ok(updated);
    }
}
