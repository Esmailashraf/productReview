package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repo.ProductRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private ProductService productService;

    @Test
    public void getProductById() {
        //arrange
        Product product = Product.builder()
                .id(1)
                .name("lap")
                .type("tech")
                .description("beau")
                .build();
        Mockito.when(productRepo.findById(1)).thenReturn(Optional.of(product));
        //act
        Product result = productService.getProductById(1);
        //assert
        Assertions.assertThat(result).isEqualTo(product);
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Mockito.verify(productRepo, Mockito.times(1)).findById(1);
    }

    @Test
    public void createProductTest() {
        //arrange
        Product product = Product.builder()
                .name("lap")
                .type("tech")
                .description("beau")
                .build();
        Product productSaved = Product.builder()
                .id(1)
                .name("lap")
                .type("tech")
                .description("beau")
                .build();
        Mockito.when(productRepo.save(product)).thenReturn(productSaved);
        //act
        Product result = productService.createProduct(product);
        //assert
        Assertions.assertThat(result).isEqualTo(productSaved);
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Mockito.verify(productRepo, Mockito.times(1)).save(product);
    }

    @Test
    public void updateProductTest() {
        //arrange
        Product product = Product.builder()
                .id(1)
                .name("lap")
                .type("tech")
                .description("beau")
                .build();

        Product productUpdated = Product.builder()
                .id(1)
                .name("lap updated")
                .type("tech")
                .description("beau updated")
                .build();
        Mockito.when(productRepo.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(productRepo.save(Mockito.any(Product.class))).thenReturn(productUpdated);
        //act
        Product result = productService.updateProduct(1, productUpdated);
        //assert
        Assertions.assertThat(result).isEqualTo(productUpdated);
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Mockito.verify(productRepo, Mockito.times(1)).save(Mockito.any(Product.class));
    }

    @Test
    public void deleteProductTest() {
        //arrange
        Mockito.doNothing().when(productRepo).deleteById(1);
        //act
        productService.deleteProduct(1);
        //assert
        Mockito.verify(productRepo, Mockito.times(1)).deleteById(1);
    }
}
