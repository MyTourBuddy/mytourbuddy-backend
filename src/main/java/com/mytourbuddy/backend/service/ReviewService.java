package com.mytourbuddy.backend.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.Review;
import com.mytourbuddy.backend.repository.ReviewRepository;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.util.IdGenerator;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdGenerator idGenerator;

    // create review
    public Review createReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review is required");
        }

        verifyProfilesExist(review);
        review.setId(idGenerator.generate("rev", reviewRepository::existsById));
        review.setCreatedAt(Instant.now());
        return reviewRepository.save(review);
    }

    public boolean verifyProfilesExist(Review review) {
        boolean guideExists = userRepository.existsById(review.getGuideId());
        boolean touristExists = userRepository.existsById(review.getTouristId());

        if (!guideExists) {
            throw new IllegalArgumentException("Guide with username " + review.getGuideId() + " not found");
        }

        if (!touristExists) {
            throw new IllegalArgumentException("Tourist with username " + review.getTouristId() + " not found");
        }

        return true;
    }

    // get all reveiws
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // get reveiw by id
    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    // get reveiws by guide id
    public List<Review> getReviewsByGuideId(String guideId) {
        return reviewRepository.findByGuideId(guideId);
    }

    // get reveiws by tourist id
    public List<Review> getReviewsByTouristId(String touristId) {
        return reviewRepository.findByTouristId(touristId);
    }

    // update review
    public Review updateReview(String id, Review updatedReview) {

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        Optional.ofNullable(updatedReview.getMessage())
                .filter(msg -> !msg.isEmpty())
                .ifPresent(existingReview::setMessage);

        Optional.ofNullable(updatedReview.getRating())
                .filter(rating -> rating >= 1 && rating <= 5)
                .ifPresent(existingReview::setRating);

        return reviewRepository.save(existingReview);
    }

    // delete review
    public void deleteReview(String id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Review not found");
        }
        reviewRepository.deleteById(id);
    }
}
