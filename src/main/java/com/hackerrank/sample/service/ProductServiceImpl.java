package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(final ProductRequest request) {
        var product = mapToEntity(request);
        var savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(final Long id) {
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NoSuchResourceFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(final Long id, final ProductRequest request) {
        var existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchResourceFoundException("Cannot update: Product not found"));

        existingProduct.setTitle(request.title());
        existingProduct.setPrice(request.price());
        existingProduct.setStock(request.stock());
        existingProduct.setCondition(request.condition());
        existingProduct.setImageUrls(request.imageUrls());

        return mapToResponse(productRepository.save(existingProduct));
    }

    @Override
    @Transactional
    public void deleteProductById(final Long id) {
        // Guard Clause: Check existence before deletion
        if (!productRepository.existsById(id)) {
            throw new NoSuchResourceFoundException("Cannot delete: Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<ProductResponse> getAllProducts(final Pageable pageable) {
        var page = productRepository.findAll(pageable);

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