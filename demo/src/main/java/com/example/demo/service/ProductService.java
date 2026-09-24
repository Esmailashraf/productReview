package com.example.demo.service;

import com.example.demo.Dto.product.Request.ProductRequest;
import com.example.demo.Dto.product.Response.ProductResponse;
import com.example.demo.exception.product.ProductAlreadyExistsException;
import com.example.demo.exception.product.ProductNotFoundException;
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

    public List<ProductResponse> getAllProducts(
            String name,
            String brand,
            String category) {

        return productRepository.findProducts(name, brand, category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id)
                );

        return mapToResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new ProductAlreadyExistsException("Product name already exists " + request.getName());
        }

        Product product = new Product();

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setAttributes(request.getAttributes());

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @CachePut(value = "products", key = "#id")
    public ProductResponse updateProduct(
            String id,
            ProductRequest request) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductAlreadyExistsException("Product not found with id: " + id)
                );

        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setAttributes(request.getAttributes());

        Product updatedProduct = productRepository.save(existingProduct);

        return mapToResponse(updatedProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id)
                );

        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .category(product.getCategory())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .attributes(product.getAttributes())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}