package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String title,
        BigDecimal price,
        Integer stock,
        Condition condition,
        List<String> imageUrls
) {
}