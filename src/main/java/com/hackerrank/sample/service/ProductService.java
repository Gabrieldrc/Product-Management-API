package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    /**
     * Creates a new product and persists it.
     */
    ProductResponse createProduct(final ProductRequest request);

    /**
     * Retrieves a product by its unique identifier.
     */
    ProductResponse getProductById(final Long id);

    /**
     * Updates an existing product's data.
     */
    ProductResponse updateProduct(final Long id, final ProductRequest request);

    /**
     * Removes a product from the system by ID.
     */
    void deleteProductById(final Long id);

    /**
     * Returns a paginated list of products.
     */
    PaginatedResponse<ProductResponse> getAllProducts(final Pageable pageable);

    /**
     * Deletes all products in a single batch operation.
     */
    void deleteAllProducts();
}