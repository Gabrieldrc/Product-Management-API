package com.hackerrank.sample.config;

import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.model.Product.Condition;
import com.hackerrank.sample.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@Profile("!test")
public class ProductDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductDataSeeder.class);
    private final ProductService productService;

    public ProductDataSeeder(final ProductService productService) {
        this.productService = productService;
    }

    /**
     * Seeds initial product data if the database is empty.
     * Use case: Development and staging environments.
     *
     * @param args Command line arguments.
     */
    @Override
    public void run(final String... args) {
        var existingProducts = productService.getAllProducts(PageRequest.of(0, 1));

        if (existingProducts.totalElements() > 0) {
            log.info("Database already contains data, skipping seeding.");
            return;
        }

        log.info("Starting initial product seeding...");

        var initialProducts = List.of(
                new ProductRequest(
                        "iPhone 15 Pro Max 256GB",
                        new BigDecimal("1199.99"),
                        15,
                        Condition.NEW,
                        List.of("https://example.com/iphone15-1.jpg", "https://example.com/iphone15-2.jpg")
                ),
                new ProductRequest(
                        "MacBook Pro M3 14\"",
                        new BigDecimal("1599.00"),
                        8,
                        Condition.NEW,
                        List.of("https://example.com/macbook-1.jpg")
                ),
                new ProductRequest(
                        "PlayStation 5 Console (Used - Like New)",
                        new BigDecimal("450.00"),
                        2,
                        Condition.USED,
                        List.of("https://example.com/ps5-used.jpg")
                ),
                new ProductRequest(
                        "Logitech MX Master 3S",
                        new BigDecimal("99.00"),
                        50,
                        Condition.NEW,
                        List.of("https://example.com/mouse-1.jpg")
                )
        );

        initialProducts.forEach(productService::createProduct);
        log.info("Seeding completed successfully. {} products created.", initialProducts.size());
    }
}