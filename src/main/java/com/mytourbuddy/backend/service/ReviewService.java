package com.mytourbuddy.backend.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.CreateReviewRequest;
import com.mytourbuddy.backend.dto.request.UpdateReviewRequest;
import com.mytourbuddy.backend.model.Booking;
import com.mytourbuddy.backend.model.BookingStatus;
import com.mytourbuddy.backend.model.Review;
import com.mytourbuddy.backend.repository.BookingRepository;
import com.mytourbuddy.backend.repository.ReviewRepository;
import com.mytourbuddy.backend.util.IdGenerator;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private BookingRepository bookingRepository;

    // create review
    public Review createReview(CreateReviewRequest request, String touristId) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getTouristId().equals(touristId)) {
            throw new IllegalArgumentException("Unauthorized: Booking does not belong to user");
        }

        if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("Booking is not completed");
        }

        Review review = new Review();
        review.setId(idGenerator.generate("rev", reviewRepository::existsById));
        review.setBookingId(request.getBookingId());
        review.setTouristId(touristId);
        review.setGuideId(booking.getGuideId());
        review.setMessage(request.getMessage());
        review.setRating(request.getRating());
        review.setCreatedAt(Instant.now());

        Review savedReview = reviewRepository.save(review);
        booking.setIsReviewed(true);
        bookingRepository.save(booking);
        return savedReview;
    }

    public Booking verifyCompletedBookingExists(Review review) {
        Booking booking = bookingRepository.findById(review.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (booking.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("Booking is not completed");
        }
        if (booking.getIsReviewed() != null && booking.getIsReviewed()) {
            throw new IllegalArgumentException("Review already exists for this booking");
        }
        return booking;
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
    public Review updateReview(String id, UpdateReviewRequest request, String touristId) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!existingReview.getTouristId().equals(touristId)) {
            throw new IllegalArgumentException("Unauthorized: You can only update your own reviews");
        }

        Optional.ofNullable(request.getMessage())
                .filter(msg -> !msg.trim().isEmpty())
                .ifPresent(existingReview::setMessage);

        Optional.ofNullable(request.getRating())
                .ifPresent(existingReview::setRating);

        return reviewRepository.save(existingReview);
    }

    // delete review
    public void deleteReview(String id, String touristId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (touristId != null && !review.getTouristId().equals(touristId)) {
            throw new IllegalArgumentException("Unauthorized: You can only delete your own reviews");
        }

        reviewRepository.deleteById(id);
    }
}
