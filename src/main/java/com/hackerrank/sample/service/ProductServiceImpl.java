package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // En un sistema real, no solemos recibir el ID del cliente al crear.
        // JPA genera el ID automáticamente.
        Product product = mapToEntity(request);
        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NoSuchResourceFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
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
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchResourceFoundException("Cannot delete: Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public PaginatedResponse<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);

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

    // --- Mappers Privados ---

    private Product mapToEntity(ProductRequest request) {
        return new Product(
                request.title(),
                request.price(),
                request.stock(),
                request.condition(),
                request.imageUrls()
        );
    }

    private ProductResponse mapToResponse(Product product) {
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