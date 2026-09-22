package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.service.JwtService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ReviewService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CacheManager cacheManager;

    private Product createProduct() {
        return Product.builder()
                .id(1).name("product1")
                .type("tech").description("description")
                .build();
    }

    private Review createReview(Product product, Long id, String title, String content) {
        return Review.builder().id(id).
                title(title).content(content).
                stars(5).product(product).
                build();
    }

    @Test
    public void getAllReviewsTest() throws Exception {

        // Arrange
        Product product = createProduct();

        Review review1 = createReview(product, 1L, "review1", "review 1");

        Review review2 = createReview(product, 1L, "review2", "review 2");

        List<Review> reviews = List.of(review1, review2);

        when(reviewService.getAllReviews()).thenReturn(reviews);

        // Act & Assert
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("title2"));
    }

    @Test
    public void getReviewByIdTest() throws Exception {
        //arrange
        Product product = createProduct();
        Review review1 = createReview(product, 1L, "review1", "review 1");
        when(reviewService.getReviewById(1L)).thenReturn(review1);
        //act&assert
        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("review1"))
                .andExpect(jsonPath("$.content").value("review 1"));
    }

    @Test
    public void createReviewTest() throws Exception {
        //arrange
        Product product = createProduct();
        Review review1 = createReview(product, 1L, "review1", "review 1");
        when(reviewService.createReview(eq(1), any(Review.class))).thenReturn(review1);
        //act &&assert
        mockMvc.perform(post("/api/reviews/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "review1",
                                    "content": "review 1",
                                    "stars": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("review1"));
    }

    @Test
    public void getReviewByProductIdTest() throws Exception {
        Product product = createProduct();
        Review review1 = createReview(product, 1L, "review1", "review 1");
        Review review2 = createReview(product, 1L, "review2", "review 2");
        List<Review> reviews = List.of(review1, review2);
        when(reviewService.getReviewsByProduct(1)).thenReturn(reviews);
        mockMvc.perform(get("/api/reviews/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("review1"));
    }

    @SneakyThrows
    @Test
    public void updateReviewTest() {
        Product product = createProduct();
        Review review1Updated = createReview(product, 1L, "review1 updated", "review 1 updated");
        when(reviewService.updateReview(eq(1L), any(Review.class))).thenReturn(review1Updated);

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "review1 updated",
                                    "content": "review1 updated",
                                    "stars": 5
                                }
                                """)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("review1 updated"));
    }
    @SneakyThrows
    @Test
    public void deleteReviewTest() {
        doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }
}