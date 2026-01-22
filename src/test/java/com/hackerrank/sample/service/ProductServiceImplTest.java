package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductRequest;
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
        @DisplayName("CP-01: Success creation with all fields")
        void createProduct_Success() {
            final var request = createFullRequest("iPhone 15", "1000.00");
            final var productEntity = mapToMockEntity(request, 1L);

            when(productRepository.save(any(Product.class))).thenReturn(productEntity);

            final var response = productService.createProduct(request);

            assertAll(
                    () -> assertNotNull(response.id()),
                    () -> assertEquals(request.title(), response.title()),
                    () -> assertEquals(request.description(), response.description()),
                    () -> assertEquals(request.sellerName(), response.seller().name()),
                    () -> assertEquals(request.shippingCost(), response.shipping().cost())
            );
        }

        @Test
        @DisplayName("CP-06: Success creation with empty image list")
        void createProduct_EmptyImages() {
            final var request = new ProductRequest(
                    "No Image Tech", "Desc", new BigDecimal("50"), 5, Condition.NEW,
                    Collections.emptyList(), "Store", 4.0, BigDecimal.ZERO, "Today");

            final var savedProduct = mapToMockEntity(request, 2L);

            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            final var response = productService.createProduct(request);
            assertTrue(response.imageUrls().isEmpty());
        }
    }

    @Nested
    @DisplayName("Query by ID Tests")
    class QueryTests {
        @Test
        @DisplayName("CP-07: Search found with nested info")
        void getProductById_Found() {
            final var product = Product.builder()
                    .id(10L)
                    .title("Laptop")
                    .sellerName("Apple")
                    .shippingCost(BigDecimal.TEN)
                    .build();

            when(productRepository.findById(10L)).thenReturn(Optional.of(product));

            final var response = productService.getProductById(10L);

            assertAll(
                    () -> assertEquals(10L, response.id()),
                    () -> assertEquals("Apple", response.seller().name())
            );
        }

        @Test
        @DisplayName("CP-08/09: Product not found")
        void getProductById_NotFound() {
            when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(NoSuchResourceFoundException.class, () -> productService.getProductById(999L));
        }
    }

    @Nested
    @DisplayName("Update Product Tests")
    class UpdateTests {
        @Test
        @DisplayName("CP-10: Full update success including nested fields")
        void updateProduct_Success() {
            final var targetId = 1L;
            final var existingProduct = Product.builder().id(targetId).title("Old").build();
            final var updateRequest = createFullRequest("New Name", "1500.00");

            when(productRepository.findById(targetId)).thenReturn(Optional.of(existingProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

            final var response = productService.updateProduct(targetId, updateRequest);

            assertAll(
                    () -> assertEquals("New Name", response.title()),
                    () -> assertEquals("Apple Store", response.seller().name()),
                    () -> assertEquals(new BigDecimal("1500.00"), response.price())
            );
        }
    }

    @Nested
    @DisplayName("Deletion Tests")
    class DeletionTests {
        @Test
        @DisplayName("CP-14: Deletion success")
        void deleteProduct_Success() {
            when(productRepository.existsById(1L)).thenReturn(true);
            productService.deleteProductById(1L);
            verify(productRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("CP-16: Total wipeout")
        void deleteAll_Success() {
            productService.deleteAllProducts();
            verify(productRepository, times(1)).deleteAllInBatch();
        }
    }

    @Nested
    @DisplayName("Pagination & Business Logic Tests")
    class ListTests {
        @Test
        @DisplayName("CP-17: Correct mapping of nested objects in list")
        void getAll_VerifyMapping() {
            final var product = Product.builder()
                    .sellerName("Meli Store")
                    .shippingCost(BigDecimal.ZERO)
                    .imageUrls(List.of("url1"))
                    .build();

            final var page = new PageImpl<>(List.of(product));
            when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

            final var results = productService.getAllProducts(PageRequest.of(0, 10));

            final var item = results.content().get(0);
            assertEquals("Meli Store", item.seller().name());
            assertEquals(BigDecimal.ZERO, item.shipping().cost());
        }
    }

    // Helper methods for cleaner tests
    private ProductRequest createFullRequest(final String title, final String price) {
        return new ProductRequest(
                title, "Full Description", new BigDecimal(price), 10, Condition.NEW,
                List.of("http://image.com"), "Apple Store", 4.8, new BigDecimal("10.00"), "3 days"
        );
    }

    private Product mapToMockEntity(final ProductRequest request, final Long id) {
        return Product.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .condition(request.condition())
                .imageUrls(request.imageUrls())
                .sellerName(request.sellerName())
                .sellerRating(request.sellerRating())
                .shippingCost(request.shippingCost())
                .estimatedDelivery(request.estimatedDelivery())
                .build();
    }
}