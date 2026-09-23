package com.example.demo.repo;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, String> {
    @Query(
            """
                        SELECT p FROM Product p WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%') ) )
                        AND (:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%',:brand,'%') ) )
                        AND (:category IS NULL OR LOWER(p.category) LIKE LOWER(CONCAT('%',:category,'%') ) )
                    """
    )
    List<Product> findProducts(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("category") String category
    );
}