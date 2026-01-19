package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProductById(Long id);

    PaginatedResponse<ProductResponse> getAllProducts(Pageable pageable);

    void deleteAllProducts();
}