package com.example.demo.repo;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepoTest {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ReviewRepository reviewRepo;

    @Test
    public void reviewRepo_SaveAllTest() {
        //Arrange
        Product product = Product.builder().
                name("laptop").
                type("tech").
                description("used to tech").
                build();
        Product productSaved = productRepo.save(product);
        Review review = Review.builder()
                .content("good for programmers")
                .title("feedback")
                .stars(5)
                .product(productSaved)
                .build();
        //act
        Review reviewSaved = reviewRepo.save(review);
        //assert
        Assertions.assertThat(reviewSaved).isNotNull();
    }

    @Test
    public void reviewRepo_findAllReviewByProductIdTest() {
        //Arrange
        Product product = Product.builder().
                name("laptop").
                type("tech").
                description("used to tech").
                build();
        Product productSaved = productRepo.save(product);
        Review review1 = Review.builder()
                .content("good for programmers")
                .title("feedback")
                .stars(5)
                .product(productSaved)
                .build();
        Review reviewSaved1 = reviewRepo.save(review1);
        Review review2 = Review.builder()
                .content("good for programmers")
                .title("feedback")
                .stars(5)
                .product(productSaved)
                .build();
        Review reviewSaved2 = reviewRepo.save(review2);

        //act
        List<Review> reviewList = reviewRepo.findByProductId(product.getId());
        //assert
        Assertions.assertThat(reviewList).isNotNull();
        Assertions.assertThat(reviewList.size()).isEqualTo(2);
    }

    @Test
    public void reviewRepo_findByIdTest() {
        // Arrange
        Product product = Product.builder()
                .name("laptop")
                .type("tech")
                .description("used to tech")
                .build();

        Product productSaved = productRepo.save(product);

        Review review = Review.builder()
                .content("good for programmers")
                .title("feedback")
                .stars(5)
                .product(productSaved)
                .build();

        Review reviewSaved = reviewRepo.save(review);

        // Act
        Review foundReview = reviewRepo.findById(reviewSaved.getId())
                .orElse(null);

        // Assert
        Assertions.assertThat(foundReview).isNotNull();
        Assertions.assertThat(foundReview.getId())
                .isEqualTo(reviewSaved.getId());
        Assertions.assertThat(foundReview.getTitle())
                .isEqualTo("feedback");
        Assertions.assertThat(foundReview.getStars())
                .isEqualTo(5);
    }

    @Test
    public void reviewRepo_updateTest() {
        // Arrange
        Product product1 = Product.builder()
                .name("laptop")
                .type("tech")
                .description("used to tech")
                .build();

        Product productSaved1 = productRepo.save(product1);

        Product product2 = Product.builder()
                .name("phone")
                .type("tech")
                .description("smart phone")
                .build();

        Product productSaved2 = productRepo.save(product2);

        Review review = Review.builder()
                .content("good for programmers")
                .title("feedback")
                .stars(5)
                .product(productSaved1)
                .build();

        Review reviewSaved = reviewRepo.save(review);

        // Act
        reviewSaved.setTitle("Excellent feedback");
        reviewSaved.setContent("Very good product");
        reviewSaved.setStars(4);
        reviewSaved.setProduct(productSaved2);

        Review updatedReview = reviewRepo.save(reviewSaved);

        // Assert
        Assertions.assertThat(updatedReview.getTitle())
                .isEqualTo("Excellent feedback");

        Assertions.assertThat(updatedReview.getContent())
                .isEqualTo("Very good product");

        Assertions.assertThat(updatedReview.getStars())
                .isEqualTo(4);

        Assertions.assertThat(updatedReview.getProduct().getId())
                .isEqualTo(productSaved2.getId());
    }
    @Test
    public void reviewRepo_deleteTest() {
        // Arrange
        Product product = Product.builder()
                .name("laptop")
                .type("tech")
                .description("used to tech")
                .build();

        Product productSaved = productRepo.save(product);

        Review review = Review.builder()
                .content("good for programmers")
                .title("feedback")
                .stars(5)
                .product(productSaved)
                .build();

        Review reviewSaved = reviewRepo.save(review);

        Long reviewId = reviewSaved.getId();

        // Act
        reviewRepo.deleteById(reviewId);

        // Assert
        boolean exists = reviewRepo.existsById(reviewId);

        Assertions.assertThat(exists).isFalse();
    }

}
