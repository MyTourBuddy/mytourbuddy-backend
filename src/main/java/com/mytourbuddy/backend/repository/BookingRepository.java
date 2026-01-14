package com.mytourbuddy.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytourbuddy.backend.model.Booking;
import com.mytourbuddy.backend.model.BookingStatus;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByTouristId(String touristId);

    List<Booking> findByPkgId(String pkgId);

    List<Booking> findByGuideId(String guideId);

    List<Booking> findByTouristIdAndGuideIdAndBookingStatus(String touristId, String guideId, BookingStatus status);

    List<Booking> findByTouristIdAndPkgIdAndBookingStatusNot(String touristId, String pkgId, BookingStatus status);

    List<Booking> findByTouristIdAndPkgIdAndBookingStatusIn(String touristId, String pkgId, List<BookingStatus> statuses);
}
