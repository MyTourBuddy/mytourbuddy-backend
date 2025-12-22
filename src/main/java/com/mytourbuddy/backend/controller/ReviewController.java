package com.mytourbuddy.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mytourbuddy.backend.model.Review;
import com.mytourbuddy.backend.service.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }


    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }


    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable String id) {
        return reviewService.getReviewById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }


    @GetMapping("/guide/{guideId}")
    public List<Review> getGuideReviews(@PathVariable String guideId) {
        return reviewService.getReviewsByGuideId(guideId);
    }


    @GetMapping("/tourist/{touristId}")
    public List<Review> getTouristReviews(@PathVariable String touristId) {
        return reviewService.getReviewsByTouristId(touristId);
    }


    @PutMapping("/{id}")
    public Review updateReview(@PathVariable String id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }


    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return "Review deleted successfully";
    }
}
