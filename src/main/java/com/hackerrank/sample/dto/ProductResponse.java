package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Detailed representation of a product including seller and shipping info")
public record ProductResponse(
        @Schema(description = "Unique identifier", example = "1025")
        Long id,

        @Schema(description = "Product title", example = "iPhone 15 Pro Max")
        String title,

        @Schema(description = "Detailed description", example = "Long text description...")
        String description,

        @Schema(description = "Current price", example = "1250.50")
        BigDecimal price,

        @Schema(description = "Stock units", example = "50")
        Integer stock,

        @Schema(description = "Sale condition", example = "NEW")
        Condition condition,

        @Schema(description = "Image gallery")
        List<String> imageUrls,

        @Schema(description = "Consolidated seller information")
        SellerInfo seller,

        @Schema(description = "Consolidated shipping information")
        ShippingInfo shipping
) {
    /**
     * Nested record for Seller representation
     */
    @Schema(description = "Information about the product seller")
    public record SellerInfo(
            @Schema(example = "Apple Store") String name,
            @Schema(example = "4.8") Double rating
    ) {
    }

    /**
     * Nested record for Shipping representation
     */
    @Schema(description = "Information about shipping costs and delivery")
    public record ShippingInfo(
            @Schema(example = "0.00") BigDecimal cost,
            @Schema(example = "Tomorrow") String estimatedDelivery
    ) {
    }
}

