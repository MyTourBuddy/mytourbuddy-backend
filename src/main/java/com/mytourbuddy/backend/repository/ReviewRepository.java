package com.mytourbuddy.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytourbuddy.backend.model.Review;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByGuideId(String guideId);
    List<Review> findByTouristId(String touristId);
}
