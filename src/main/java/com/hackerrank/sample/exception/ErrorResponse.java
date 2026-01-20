package com.hackerrank.sample.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estructura estándar de error para toda la API")
public record ErrorResponse(
        @Schema(description = "Código de estado HTTP", example = "400")
        int status,
        @Schema(description = "Mensaje detallado del error", example = "El campo 'price' es obligatorio")
        String message,
        @Schema(description = "Timestamp en milisegundos", example = "1705742400000")
        long timestamp
) {
}
