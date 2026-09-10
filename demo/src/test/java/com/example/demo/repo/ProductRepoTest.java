package com.example.demo.repo;

import com.example.demo.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Test
    public void ProductRepo_saveallTest() {

        // Arrange
        Product product = Product.builder()
                .name("laptop")
                .description("this tech tool")
                .type("tech")
                .build();

        // Act
        Product productSaved = productRepo.save(product);

        // Assert
        Assertions.assertThat(productSaved).isNotNull();
        Assertions.assertThat(productSaved.getId()).isGreaterThan(0);
    }

    @Test
    public void ProductRepo_FindAllTest() {
        // Arrange
        Product product1 = Product.builder()
                .name("laptop")
                .description("this tech tool")
                .type("tech")
                .build();
        Product product2 = Product.builder()
                .name("pc")
                .description("this tech tool")
                .type("tech")
                .build();
        Product productSaved1 = productRepo.save(product1);
        Product productSaved2 = productRepo.save(product2);
        // Act
        List<Product> productList = productRepo.findAll();


        // Assert
        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isEqualTo(2);
    }

    @Test
    public void ProductRepo_GetProductTest() {
        // Arrange
        Product product1 = Product.builder()
                .name("laptop")
                .description("this tech tool")
                .type("tech")
                .build();

        Product productSaved1 = productRepo.save(product1);

        // Act
        Product product = productRepo.findById(productSaved1.getId()).get();


        // Assert
        Assertions.assertThat(product).isNotNull();
    }

    @Test
    public void ProductRepo_GetProductNameTest() {
        // Arrange
        Product product1 = Product.builder()
                .name("laptop")
                .description("this tech tool")
                .type("tech")
                .build();

        Product productSaved1 = productRepo.save(product1);

        // Act
        Product product = productRepo.findByName(productSaved1.getName()).get();


        // Assert
        Assertions.assertThat(product).isNotNull();
    }

    @Test
    public void ProductRepo_UpdateProductTest() {

        // Arrange
        Product product = Product.builder()
                .name("laptop")
                .description("this tech tool")
                .type("tech")
                .build();

        Product productSaved = productRepo.save(product);

        // Act
        productSaved.setName("gaming laptop");
        productSaved.setDescription("powerful gaming laptop");
        productSaved.setType("gaming");

        Product updatedProduct = productRepo.save(productSaved);

        // Assert
        Assertions.assertThat(updatedProduct).isNotNull();
        Assertions.assertThat(updatedProduct.getId())
                .isEqualTo(productSaved.getId());

        Assertions.assertThat(updatedProduct.getName())
                .isEqualTo("gaming laptop");

        Assertions.assertThat(updatedProduct.getDescription())
                .isEqualTo("powerful gaming laptop");

        Assertions.assertThat(updatedProduct.getType())
                .isEqualTo("gaming");
    }

    @Test
    public void ProductRepo_DeleteProductTest() {

        // Arrange
        Product product = Product.builder()
                .name("laptop")
                .description("this tech tool")
                .type("tech")
                .build();

        Product productSaved = productRepo.save(product);

        // Act
        productRepo.deleteById(productSaved.getId());

        // Assert
        Assertions.assertThat(
                productRepo.findById(productSaved.getId())
        ).isEmpty();
    }


}

