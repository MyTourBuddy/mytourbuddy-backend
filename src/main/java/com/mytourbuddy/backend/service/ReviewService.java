package com.mytourbuddy.backend.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.Review;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.ReviewRepository;
import com.mytourbuddy.backend.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;


    public Review createReview(Review review) {

        if (review.getGuideId() == null || review.getTouristId() == null) {
            throw new IllegalArgumentException("Guide ID and Tourist ID are required");
        }

        Optional<User> guide = userRepository.findById(review.getGuideId());
        Optional<User> tourist = userRepository.findById(review.getTouristId());

        if (guide.isEmpty()) {
            throw new IllegalArgumentException("Guide not found");
        }
        if (tourist.isEmpty()) {
            throw new IllegalArgumentException("Tourist not found");
        }

        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        review.setCreatedAt(Instant.now());

        return reviewRepository.save(review);
    }


    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }


    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }


    public List<Review> getReviewsByGuideId(String guideId) {
        return reviewRepository.findByGuideId(guideId);
    }


    public List<Review> getReviewsByTouristId(String touristId) {
        return reviewRepository.findByTouristId(touristId);
    }


    public Review updateReview(String id, Review updatedReview) {

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (updatedReview.getMessage() != null && !updatedReview.getMessage().isEmpty()) {
            existingReview.setMessage(updatedReview.getMessage());
        }

        if (updatedReview.getRating() != null && updatedReview.getRating() >= 1 && updatedReview.getRating() <= 5) {
            existingReview.setRating(updatedReview.getRating());
        }

        if (updatedReview.getImage() != null) {
            existingReview.setImage(updatedReview.getImage());
        }

        return reviewRepository.save(existingReview);
    }


    public void deleteReview(String id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Review not found");
        }
        reviewRepository.deleteById(id);
    }
}
