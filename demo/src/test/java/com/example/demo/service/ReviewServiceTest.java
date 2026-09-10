package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepo;
    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private ProductService productService;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void ReviewService_SaveAllTest() {
        //arrange

        Product productSaved = Product.builder()
                .id(1)
                .name("laptop")
                .type("tech")
                .description("used for gaming")
                .build();
        Mockito.when(productRepo.findById(1))
                .thenReturn(Optional.of(productSaved));
        Review review = Review.builder()
                .title("title")
                .content("content")
                .product(productSaved)
                .stars(5)
                .build();

        Review reviewSaved = Review.builder()
                .id(1L)
                .title("title")
                .content("content")
                .product(productSaved)
                .stars(5)
                .build();
        Mockito.when(reviewRepo.save(review)).thenReturn(reviewSaved);
        //act
        Review resutl = reviewService.createReview(productSaved.getId(), review);
        //Assert
        Assertions.assertThat(resutl.getTitle()).isEqualTo("title");
        Assertions.assertThat(resutl.getContent()).isEqualTo("content");
        Assertions.assertThat(resutl.getProduct()).isEqualTo(productSaved);
        Mockito.verify(reviewRepo).save(review);

    }

    @Test
    public void ReviewService_FindAllTest() {
        //arrange
        Product product = new Product(1, "laptop", "used for gaming", "tech");
        List<Review> reviews = new ArrayList<>(List.of(
                new Review(1L, "feedback", "good laptop", 5, product),
                new Review(2L, "feedback", "good laptop", 5, product))
        );
        Mockito.when(reviewRepo.findAll()).thenReturn(reviews);
        // act
        List<Review> result = reviewService.getAllReviews();

        //assert
        Assertions.assertThat(result).isEqualTo(reviews);
        Assertions.assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void ReviewService_FindByIdTest() {
        //arrange
        Product product = new Product(1, "laptop", "used for gaming", "tech");
        Review review = new Review(1L, "feedback", "good laptop", 5, product);
        Mockito.when(reviewRepo.findById(1L)).thenReturn(Optional.of(review));
        //act
        Review result = reviewService.getReviewById(1L);
        //assert
        Assertions.assertThat(result).isEqualTo(review);
        Assertions.assertThat(result.getProduct()).isEqualTo(product);
        Mockito.verify(reviewRepo).findById(1L);
    }

    @Test
    public void ReviewService_FindByProductIdTest() {

        // Arrange
        Product product = new Product(
                1,
                "laptop",
                "used for gaming",
                "tech"
        );

        Review review = new Review(
                1L,
                "feedback",
                "good laptop",
                5,
                product
        );

        Mockito.when(reviewRepo.findByProductId(1))
                .thenReturn(List.of(review));

        // Act
        List<Review> result = reviewService.getReviewsByProduct(1);

        // Assert
        Assertions.assertThat(result).isEqualTo(List.of(review));

        // Verify repository was called
        Mockito.verify(reviewRepo, Mockito.times(1))
                .findByProductId(1);
    }

    @Test
    public void ReviewService_UpdateTest() {

        // Arrange
        Review review = Review.builder()
                .id(1L)
                .title("beforeUpdated")
                .content("content")
                .stars(8)
                .build();

        Product productUpdated = Product.builder()
                .name("updated")
                .description("updated description")
                .build();

        Review reviewUpdated = Review.builder()
                .id(1L)
                .title("updated")
                .content("updated description")
                .product(productUpdated)
                .stars(9)
                .build();

        Mockito.when(reviewRepo.findById(1L))
                .thenReturn(Optional.of(review));

        Mockito.when(reviewRepo.save(Mockito.any(Review.class)))
                .thenReturn(reviewUpdated);

        // Act
        Review result = reviewService.updateReview(1L, reviewUpdated);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getTitle()).isEqualTo("updated");
        Assertions.assertThat(result.getContent()).isEqualTo("updated description");
        Assertions.assertThat(result.getStars()).isEqualTo(9);
        Assertions.assertThat(result.getProduct()).isEqualTo(productUpdated);

        // Verify
        Mockito.verify(reviewRepo, Mockito.times(1))
                .findById(1L);

        Mockito.verify(reviewRepo, Mockito.times(1))
                .save(Mockito.any(Review.class));
    }

    @Test
    public void ReviewService_DeleteTest() {

        // Arrange
        Mockito.when(reviewRepo.existsById(1L))
                .thenReturn(true);

        // Act
        Review result = reviewService.deleteReview(1L);

        // Assert
        Assertions.assertThat(result).isNull();

        // Verify
        Mockito.verify(reviewRepo, Mockito.times(1))
                .existsById(1L);

        Mockito.verify(reviewRepo, Mockito.times(1))
                .deleteById(1L);
    }

}
