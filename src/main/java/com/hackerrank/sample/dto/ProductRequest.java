package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Payload for creating or updating a product with full detail")
public record ProductRequest(
        @Schema(description = "Commercial name", example = "iPhone 15 Pro Max", maxLength = 100)
        @NotBlank(message = "Title is mandatory")
        @Size(max = 100)
        String title,

        @Schema(description = "Full product description for detail page", example = "The latest iPhone with titanium body")
        @Size(max = 2000)
        String description,

        @Schema(description = "Unit price", example = "1250.50")
        @NotNull(message = "Price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 12, fraction = 2)
        BigDecimal price,

        @Schema(description = "Available inventory units", example = "50")
        @NotNull(message = "Stock is mandatory")
        @Min(0)
        Integer stock,

        @Schema(description = "Physical condition", example = "NEW")
        @NotNull(message = "Condition is mandatory (NEW or USED)")
        Condition condition,

        @Schema(description = "Gallery of image URLs", example = "[\"https://cdn.example.com/p1.jpg\"]")
        List<@NotBlank String> imageUrls,

        @Schema(description = "Full name of the seller", example = "Apple Official Store")
        @NotBlank(message = "Seller name is mandatory")
        String sellerName,

        @Schema(description = "Seller rating from 0.0 to 5.0", example = "4.8")
        @DecimalMin("0.0") @DecimalMax("5.0")
        Double sellerRating,

        @Schema(description = "Cost of shipping", example = "15.00")
        @NotNull(message = "Shipping cost is mandatory")
        @DecimalMin("0.0")
        BigDecimal shippingCost,

        @Schema(description = "Human-readable delivery estimate", example = "Arrives by Friday")
        @NotBlank(message = "Estimated delivery is mandatory")
        String estimatedDelivery
) {
}