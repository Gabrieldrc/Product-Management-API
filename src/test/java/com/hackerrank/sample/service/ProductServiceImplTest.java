package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.Product.Condition;
import com.hackerrank.sample.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Nested
    @DisplayName("Product Creation Tests")
    class CreationTests {
        @Test
        @DisplayName("CP-01: Success creation")
        void createProduct_Success() {
            ProductRequest request = new ProductRequest("iPhone 15", new BigDecimal("1000"), 10, Condition.NEW, List.of("url1"));
            Product savedProduct = new Product(request.title(), request.price(), request.stock(), request.condition(), request.imageUrls());
            savedProduct.setId(1L);

            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            ProductResponse response = productService.createProduct(request);

            assertAll(
                    () -> assertNotNull(response.id()),
                    () -> assertEquals(request.title(), response.title()),
                    () -> assertEquals(request.price(), response.price())
            );
        }

        @Test
        @DisplayName("CP-06: Success creation with empty image list")
        void createProduct_EmptyImages() {
            ProductRequest request = new ProductRequest("No Image Tech", new BigDecimal("50"), 5, Condition.NEW, Collections.emptyList());
            Product savedProduct = new Product(request.title(), request.price(), request.stock(), request.condition(), request.imageUrls());
            savedProduct.setId(2L);

            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            ProductResponse response = productService.createProduct(request);
            assertTrue(response.imageUrls().isEmpty());
        }
    }

    @Nested
    @DisplayName("Query by ID Tests")
    class QueryTests {
        @Test
        @DisplayName("CP-07: Search found")
        void getProductById_Found() {
            Product product = new Product("Laptop", new BigDecimal("1500"), 5, Condition.NEW, List.of());
            product.setId(10L);
            when(productRepository.findById(10L)).thenReturn(Optional.of(product));

            ProductResponse response = productService.getProductById(10L);

            assertEquals(10L, response.id());
            assertEquals("Laptop", response.title());
        }

        @Test
        @DisplayName("CP-08/09: Product not found or invalid ID")
        void getProductById_NotFound() {
            when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(NoSuchResourceFoundException.class, () -> productService.getProductById(999L));
        }
    }

    @Nested
    @DisplayName("Update Product Tests")
    class UpdateTests {
        @Test
        @DisplayName("CP-10: Full update success")
        void updateProduct_Success() {
            Long targetId = 1L;
            Product existingProduct = new Product("Old Name", new BigDecimal("10"), 1, Condition.USED, List.of());
            existingProduct.setId(targetId);

            ProductRequest updateRequest = new ProductRequest("New Name", new BigDecimal("20"), 2, Condition.NEW, List.of("new_url"));

            when(productRepository.findById(targetId)).thenReturn(Optional.of(existingProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

            ProductResponse response = productService.updateProduct(targetId, updateRequest);

            assertAll(
                    () -> assertEquals("New Name", response.title()),
                    () -> assertEquals(Condition.NEW, response.condition()),
                    () -> assertEquals(new BigDecimal("20"), response.price())
            );
        }

        @Test
        @DisplayName("CP-12: Update non-existent")
        void updateProduct_NotFound() {
            when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
            ProductRequest request = new ProductRequest("N/A", BigDecimal.ONE, 1, Condition.NEW, List.of());

            assertThrows(NoSuchResourceFoundException.class, () -> productService.updateProduct(1L, request));
        }
    }

    @Nested
    @DisplayName("Deletion Tests")
    class DeletionTests {
        @Test
        @DisplayName("CP-14: Single deletion success")
        void deleteProduct_Success() {
            when(productRepository.existsById(1L)).thenReturn(true);

            productService.deleteProductById(1L);

            verify(productRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("CP-15: Delete non-existent throws exception")
        void deleteProduct_NotFound() {
            when(productRepository.existsById(1L)).thenReturn(false);
            assertThrows(NoSuchResourceFoundException.class, () -> productService.deleteProductById(1L));
        }

        @Test
        @DisplayName("CP-16: Total wipeout")
        void deleteAll_Success() {
            productService.deleteAllProducts();
            verify(productRepository, times(1)).deleteAllInBatch();
        }
    }

    @Nested
    @DisplayName("List Tests")
    class ListTests {
        @Test
        @DisplayName("CP-17: Multiple elements with pagination")
        void getAll_Multiple() {
            List<Product> products = List.of(new Product(), new Product());
            Page<Product> page = new PageImpl<>(products);

            when(productRepository.findAll(any(Pageable.class))).thenReturn(page);


            PaginatedResponse<ProductResponse> results = productService.getAllProducts(PageRequest.of(0, 10));

            assertEquals(2, results.content().size());
        }

        @Test
        @DisplayName("CP-18: Empty list with pagination")
        void getAll_Empty() {
            when(productRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

            PaginatedResponse<ProductResponse> results = productService.getAllProducts(PageRequest.of(0, 10));

            assertNotNull(results);
            assertTrue(results.content().isEmpty()); // El contenido es el que está vacío
            assertEquals(0, results.totalElements()); // El total de elementos es 0
        }
    }
}