package com.mytourbuddy.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.dto.request.CreateReviewRequest;
import com.mytourbuddy.backend.dto.request.UpdateReviewRequest;
import com.mytourbuddy.backend.model.Review;
import com.mytourbuddy.backend.security.CustomUserDetails;
import com.mytourbuddy.backend.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody CreateReviewRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String touristId = userDetails.getUserId();

        Review createdReview = reviewService.createReview(request, touristId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guides/{guideId}")
    public ResponseEntity<List<Review>> getReviewByGuideId(@PathVariable String guideId) {
        List<Review> reviews = reviewService.getReviewsByGuideId(guideId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/tourists/{touristId}")
    public ResponseEntity<List<Review>> getReviewByTouristId(@PathVariable String touristId) {
        List<Review> reviews = reviewService.getReviewsByTouristId(touristId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id,
            @Valid @RequestBody UpdateReviewRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String touristId = userDetails.getUserId();

        try {
            Review updatedReview = reviewService.updateReview(id, request, touristId);
            return ResponseEntity.ok(updatedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String touristId = userDetails.getUserId();

        // check user is admin
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        try {
            reviewService.deleteReview(id, isAdmin ? null : touristId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
