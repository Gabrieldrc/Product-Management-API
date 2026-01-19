package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotBlank(message = "Title is mandatory")
        String title,

        @NotNull(message = "Price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price,

        @NotNull(message = "Stock is mandatory")
        @Min(0)
        Integer stock,

        @NotNull(message = "Condition is mandatory")
        Condition condition,

        List<String> imageUrls
) {
}