package com.mytourbuddy.backend.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.BookingRequest;
import com.mytourbuddy.backend.dto.request.BookingUpdateRequest;
import com.mytourbuddy.backend.dto.response.BookingResponse;
import com.mytourbuddy.backend.mapper.BookingMapper;
import com.mytourbuddy.backend.model.Booking;
import com.mytourbuddy.backend.model.BookingStatus;
import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.model.PackageStatus;
import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.BookingRepository;
import com.mytourbuddy.backend.repository.PackageRepository;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.util.IdGenerator;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private IdGenerator idGenerator;

    public BookingResponse createBooking(BookingRequest request, String userId) {
        Package pkg = packageRepository.findById(request.getPkgId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        if (pkg.getStatus() != PackageStatus.ACTIVE) {
            throw new RuntimeException("Package is not available");
        }

        User tourist = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> existingBookings = bookingRepository.findByTouristIdAndPkgIdAndBookingStatusIn(tourist.getId(),
                request.getPkgId(), List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));

        if (!existingBookings.isEmpty()) {
            throw new RuntimeException(
                    "You already have an active booking for this package. You cannot book the same package multiple times.");
        }

        Booking booking = bookingMapper.toEntity(request, tourist.getId());
        booking.setId(idGenerator.generate("bkg", bookingRepository::existsById));
        booking.setGuideId(pkg.getGuideId());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCreatedAt(Instant.now());

        Booking saved = bookingRepository.save(booking);

        return bookingMapper.toResponse(saved);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getTouristBookings(String touristId) {
        return bookingRepository.findByTouristId(touristId);
    }

    public List<Booking> getGuideBookings(String guideId) {
        return bookingRepository.findByGuideId(guideId);
    }

    public Booking getBookingById(String bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return booking;
    }

    public BookingResponse updateBooking(String bookingId, BookingUpdateRequest request, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BookingStatus newStatus = request.getBookingStatus();

        if (user.getRole() == Role.GUIDE) {
            Package pkg = packageRepository.findById(booking.getPkgId())
                    .orElseThrow(() -> new RuntimeException("Package not found"));

            if (!pkg.getGuideId().equals(userId)) {
                throw new RuntimeException("You can only update bookings for your own packages");
            }

            if (null == newStatus) {
                throw new RuntimeException("Guides can only confirm or cancel pending bookings");
            } else
                switch (newStatus) {
                    case CONFIRMED -> {
                        if (booking.getBookingStatus() != BookingStatus.PENDING) {
                            throw new RuntimeException("Guides can only confirm pending bookings");
                        }
                    }
                    case COMPLETED -> {
                        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
                            throw new RuntimeException("Guides can only complete confirmed bookings");
                        }
                    }
                    case CANCELLED -> {
                        if (booking.getBookingStatus() != BookingStatus.PENDING) {
                            throw new RuntimeException("Guides can only cancel pending bookings");
                        }
                    }
                    default -> throw new RuntimeException("Guides can only confirm or cancel pending bookings");
                }

        } else if (user.getRole() == Role.TOURIST) {
            if (newStatus != BookingStatus.CANCELLED) {
                throw new RuntimeException("Tourists can only cancel bookings");
            }

            if (!booking.getTouristId().equals(userId)) {
                throw new RuntimeException("You can only cancel your own bookings");
            }

            if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
                throw new RuntimeException("You cannot cancel a completed booking");
            }
        }

        // Use mapper to update (following UserMapper pattern)
        bookingMapper.updateEntityFromRequest(request, booking);

        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }
}
