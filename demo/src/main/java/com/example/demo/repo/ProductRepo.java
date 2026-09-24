package com.example.demo.repo;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, String> {
    @Query("""
                SELECT p FROM Product p
                WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(:name, ''), '%'))
                AND LOWER(p.brand) LIKE LOWER(CONCAT('%', COALESCE(:brand, ''), '%'))
                AND LOWER(p.category) LIKE LOWER(CONCAT('%', COALESCE(:category, ''), '%'))
            """)
    List<Product> findProducts(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("category") String category
    );

    boolean existsByName(String name);
}