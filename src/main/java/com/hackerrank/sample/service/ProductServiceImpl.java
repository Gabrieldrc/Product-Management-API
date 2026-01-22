package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(final ProductRequest request) {
        log.info("Processing product creation: '{}'", request.title());
        var savedProduct = productRepository.save(mapToEntity(request));
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(final Long id) {
        log.debug("Fetching product details for ID: {}", id);
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    log.warn("Lookup failed: Product ID {} not found", id);
                    return new NoSuchResourceFoundException("Product not found");
                });
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(final Long id, final ProductRequest request) {
        log.info("Updating product ID: {} - New title: '{}'", id, request.title());
        var existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update failed: Product ID {} not found", id);
                    return new NoSuchResourceFoundException("Cannot update: Product not found");
                });

        existingProduct.setTitle(request.title());
        existingProduct.setPrice(request.price());
        existingProduct.setStock(request.stock());
        existingProduct.setCondition(request.condition());
        existingProduct.setImageUrls(request.imageUrls());

        var updated = productRepository.save(existingProduct);
        log.info("Product ID: {} updated successfully", id);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProductById(final Long id) {
        log.info("Attempting to delete product ID: {}", id);
        if (!productRepository.existsById(id)) {
            log.error("Deletion failed: Product ID {} does not exist", id);
            throw new NoSuchResourceFoundException("Cannot delete: Product not found");
        }
        productRepository.deleteById(id);
        log.info("Product ID: {} deleted successfully", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<ProductResponse> getAllProducts(final Pageable pageable) {
        log.debug("Fetching paginated products. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        var page = productRepository.findAll(pageable);

        log.info("Retrieved {} products (Total: {})", page.getNumberOfElements(), page.getTotalElements());
        return new PaginatedResponse<>(
                page.getContent().stream().map(this::mapToResponse).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    @Transactional
    public void deleteAllProducts() {
        productRepository.deleteAllInBatch();
    }

    private Product mapToEntity(final ProductRequest request) {
        return new Product(
                request.title(),
                request.price(),
                request.stock(),
                request.condition(),
                request.imageUrls()
        );
    }

    private ProductResponse mapToResponse(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getStock(),
                product.getCondition(),
                product.getImageUrls()
        );
    }
}