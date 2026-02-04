package com.mytourbuddy.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytourbuddy.backend.model.Review;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByGuideId(String guideId);

    List<Review> findByTouristId(String touristId);

    boolean existsByBookingId(String bookingId);

    Optional<Review> findByBookingId(String bookingId);
}
