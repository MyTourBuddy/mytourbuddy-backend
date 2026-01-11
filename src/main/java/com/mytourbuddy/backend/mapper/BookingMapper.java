package com.mytourbuddy.backend.mapper;

import org.springframework.stereotype.Component;

import com.mytourbuddy.backend.dto.request.BookingRequest;
import com.mytourbuddy.backend.dto.request.BookingUpdateRequest;
import com.mytourbuddy.backend.dto.response.BookingResponse;
import com.mytourbuddy.backend.model.Booking;

@Component
public class BookingMapper {
    public Booking toEntity(BookingRequest request, String touristId) {
        Booking booking = new Booking();
        booking.setPkgId(request.getPkgId());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setBookingDate(request.getBookingDate());
        booking.setTouristId(touristId);
        return booking;
    }

    public BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setTouristId(booking.getTouristId());
        response.setPkgId(booking.getPkgId());
        response.setTotalPrice(booking.getTotalPrice());
        response.setBookingStatus(booking.getBookingStatus());
        response.setCreatedAt(booking.getCreatedAt());
        return response;
    }

    public void updateEntityFromRequest(BookingUpdateRequest request, Booking booking) {
        if (request.getBookingStatus() != null) {
            booking.setBookingStatus(request.getBookingStatus());
        }
    }
}
