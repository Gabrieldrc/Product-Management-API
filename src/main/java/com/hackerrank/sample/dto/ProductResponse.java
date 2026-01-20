package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Representación detallada de un producto en el catálogo")
public record ProductResponse(
        @Schema(description = "Identificador único generado por el sistema", example = "1025")
        Long id,

        @Schema(description = "Título del producto", example = "iPhone 15 Pro Max 256GB")
        String title,

        @Schema(description = "Precio actual de venta", example = "1250.50")
        BigDecimal price,

        @Schema(description = "Stock remanente", example = "50")
        Integer stock,

        @Schema(description = "Condición de venta", example = "NEW")
        Condition condition,

        @Schema(description = "Galería de imágenes")
        List<String> imageUrls
) {
}