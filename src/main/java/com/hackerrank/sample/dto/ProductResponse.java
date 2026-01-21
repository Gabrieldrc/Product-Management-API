package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Detailed representation of a product in the catalog")
public record ProductResponse(
        @Schema(description = "Unique system identifier", example = "1025")
        Long id,

        @Schema(description = "Product title", example = "iPhone 15 Pro Max")
        String title,

        @Schema(description = "Current selling price", example = "1250.50")
        BigDecimal price,

        @Schema(description = "Remaining stock units", example = "50")
        Integer stock,

        @Schema(description = "Sale condition", example = "NEW")
        Condition condition,

        @Schema(description = "Image gallery")
        List<String> imageUrls
) {
}