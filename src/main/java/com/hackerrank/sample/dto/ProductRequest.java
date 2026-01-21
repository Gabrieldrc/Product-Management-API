package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Payload for creating or updating a product")
public record ProductRequest(
        @Schema(description = "Commercial name of the product", example = "iPhone 15 Pro Max", maxLength = 100)
        @NotBlank(message = "Title is mandatory")
        @Size(max = 100)
        String title,

        @Schema(description = "Unit price in local currency", example = "1250.50")
        @NotNull(message = "Price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        @Digits(integer = 12, fraction = 2)
        BigDecimal price,

        @Schema(description = "Available inventory units", example = "50")
        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock,

        @Schema(description = "Physical condition of the product", example = "NEW")
        @NotNull(message = "Condition is mandatory (NEW or USED)")
        Condition condition,

        @Schema(description = "Gallery of image URLs from CDN", example = "[\"https://cdn.example.com/p1.jpg\"]")
        List<@NotBlank(message = "Image URL cannot be blank") String> imageUrls
) {
}