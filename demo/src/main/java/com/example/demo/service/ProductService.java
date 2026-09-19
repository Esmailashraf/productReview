package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repo.ProductRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepository;

    public ProductService(ProductRepo productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @CachePut(value = "products", key = "#id")

    public Product updateProduct(int id, Product updatedProduct) {

        Product existingProduct = productRepository.findById(id)
                .orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setType(updatedProduct.getType());

        return productRepository.save(existingProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}