//package com.example.demo.service;
//
//import com.example.demo.model.Product;
//import com.example.demo.model.Review;
//import com.example.demo.repo.ProductRepo;
//import com.example.demo.repo.ReviewRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ReviewService {
//
//    private final ReviewRepository reviewRepository;
//    private final ProductRepo productRepository;
//
//    public ReviewService(
//            ReviewRepository reviewRepository,
//            ProductRepo productRepository
//    ) {
//        this.reviewRepository = reviewRepository;
//        this.productRepository = productRepository;
//    }
//
//    public Review createReview(int productId, Review review) {
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() ->
//                        new RuntimeException("Product not found")
//                );
//
//        review.setProduct(product);
//
//        return reviewRepository.save(review);
//    }
//
//    public List<Review> getAllReviews() {
//        return reviewRepository.findAll();
//    }
//
//    public Review getReviewById(Long id) {
//        return reviewRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Review not found")
//                );
//    }
//
//    public List<Review> getReviewsByProduct(int productId) {
//        return reviewRepository.findByProductId(productId);
//    }
//
//    public Review updateReview(Long id, Review updatedReview) {
//
//        Review review = reviewRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Review not found")
//                );
//
//        review.setTitle(updatedReview.getTitle());
//        review.setContent(updatedReview.getContent());
//        review.setStars(updatedReview.getStars());
//
//        return reviewRepository.save(review);
//    }
//
//    public Review deleteReview(Long id) {
//
//        if (!reviewRepository.existsById(id)) {
//            throw new RuntimeException("Review not found");
//        }
//
//        reviewRepository.deleteById(id);
//        return null;
//    }
//}