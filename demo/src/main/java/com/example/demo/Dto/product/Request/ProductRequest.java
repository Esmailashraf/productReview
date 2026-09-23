package com.example.demo.Dto.product.Request;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;

    private String brand;
    private String category;
    private String description;
    private String imageUrl;
    private Map<String, Object> attributes;
}
