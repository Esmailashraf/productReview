package com.example.demo.controller;

import com.example.demo.Dto.product.Request.ProductRequest;
import com.example.demo.Dto.product.Response.GetAllProducts;
import com.example.demo.Dto.product.Response.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public GetAllProducts getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return productService.getAllProducts(
                name,
                brand,
                category,
                pageNo,
                pageSize
        );
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestPart("request") ProductRequest request, @RequestParam("image") MultipartFile image) {

        return productService.createProduct(request,image);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request) {

        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {

        productService.deleteProduct(id);
    }
}