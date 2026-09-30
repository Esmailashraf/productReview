package com.example.demo.Dto.product.Response;

import com.example.demo.model.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class GetAllProducts {
    private List<ProductResponse> products;
    private int pageNo;
    private int pageSize;
    private int totalPage;
}
