package com.example.demo.service;

import com.example.demo.Dto.product.Request.ProductRequest;
import com.example.demo.Dto.product.Response.GetAllProducts;
import com.example.demo.Dto.product.Response.ProductResponse;
import com.example.demo.exception.product.ProductAlreadyExistsException;
import com.example.demo.exception.product.ProductNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repo.ProductRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepository;
    private final MinioService minioService;

    public ProductService(ProductRepo productRepository, MinioService minioService) {
        this.productRepository = productRepository;
        this.minioService = minioService;
    }

    public GetAllProducts getAllProducts(
            String name,
            String brand,
            String category,
            int pageNo,
            int pageSize

    ) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> productPage = productRepository.findProducts(name, brand, category, pageable);
        List<ProductResponse> products = productPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        GetAllProducts getAllProducts = GetAllProducts.builder().products(products)
                .pageNo(productPage.getNumber()).pageSize(productPage.getSize()).totalPage(productPage.getTotalPages()).build();
        return getAllProducts;

    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id)
                );

        return mapToResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request, MultipartFile image) {
        if (productRepository.existsByName(request.getName())) {
            throw new ProductAlreadyExistsException("Product name already exists " + request.getName());
        }
        String imageUrl = minioService.uploadFile(image);

        Product product = new Product();

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setImageUrl(imageUrl);
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