package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Review> createReview(
            @PathVariable int productId,
            @RequestBody Review review
    ) {

        Review savedReview =
                reviewService.createReview(productId, review);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedReview);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {

        return ResponseEntity.ok(
                reviewService.getAllReviews()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                reviewService.getReviewById(id)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(
            @PathVariable int productId
    ) {

        return ResponseEntity.ok(
                reviewService.getReviewsByProduct(productId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long id,
            @RequestBody Review review
    ) {

        return ResponseEntity.ok(
                reviewService.updateReview(id, review)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id
    ) {

        reviewService.deleteReview(id);

        return ResponseEntity.noContent().build();
    }
}