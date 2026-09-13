package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.JwtService;
import com.example.demo.service.ProductService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private JwtService jwtService;

    @SneakyThrows
    @Test
    public void ProductController_CreateTest() {

        // Arrange
        Product productSaved = Product.builder()
                .id(1)
                .name("Test Product")
                .description("Test Product")
                .type("tech")
                .build();

        when(productService.createProduct(any(Product.class)))
                .thenReturn(productSaved);
        //act
        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Test Product",
                                            "description": "Test Product",
                                            "type": "tech"
                                        }
                                        """)
                )
                //assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    public void getAllProductsTest() throws Exception {
        //arrange
        Product product1 = Product.builder()
                .id(1)
                .name("Laptop")
                .type("Technology")
                .description("Gaming laptop")
                .build();

        Product product2 = Product.builder()
                .id(2)
                .name("Phone")
                .type("Technology")
                .description("Smart phone")
                .build();
        List<Product> products = List.of(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);
        //act
        mockMvc.perform(get("/api/products"))
                //assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1));

    }

    @Test
    public void getProductByIdTest() throws Exception {
        //arrange
        Product product = Product.builder()
                .id(1)
                .name("Laptop")
                .type("Technology")
                .description("Gaming laptop")
                .build();
        when(productService.getProductById(1)).thenReturn(product);
        //act
        mockMvc.perform(get("/api/products/1"))
                //assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    public void updateProductTest() throws Exception {
        //arrange
        Product updatedProduct = Product.builder()
                .id(1)
                .name("Updated Laptop")
                .type("Technology")
                .description("Updated description")
                .build();
        when(productService.updateProduct(eq(1), any(Product.class))).thenReturn(updatedProduct);
        //act
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                   {
                                        "name": "Updated Laptop",
                                        "type": "Technology",
                                        "description": "Updated description"
                                   }
                                """)

                )
                //assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"));
    }

    @Test
    public void deleteProductTest() throws Exception {
        //arrange
        doNothing().when(productService).deleteProduct(eq(1));
        //act
        mockMvc.perform(delete("/api/products/1"))
        //assert
                .andExpect(status().isOk())
                .andExpect( content().string("Product deleted successfully"));

    }


}