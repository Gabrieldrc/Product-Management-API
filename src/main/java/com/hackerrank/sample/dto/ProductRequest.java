package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.Product.Condition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Contrato para la creación o actualización de productos")
public record ProductRequest(
        @Schema(description = "Nombre descriptivo del producto", example = "iPhone 15 Pro Max 256GB", maxLength = 100)
        @NotBlank(message = "Title is mandatory and cannot be empty")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        @Schema(description = "Precio unitario en moneda local", example = "1250.50")
        @NotNull(message = "Price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        @Digits(integer = 12, fraction = 2, message = "Price format must be up to 12 digits and 2 decimals")
        BigDecimal price,

        @Schema(description = "Cantidad disponible en inventario", example = "50")
        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock,

        @Schema(description = "Estado físico del producto", example = "NEW")
        @NotNull(message = "Condition is mandatory (NEW or USED)")
        Condition condition,

        @Schema(description = "Lista de URLs de imágenes alojadas en el CDN",
                example = "[\"https://cdn.meli.com/p1.jpg\"]")
        List<@NotBlank(message = "Image URL cannot be blank") String> imageUrls
) {
}