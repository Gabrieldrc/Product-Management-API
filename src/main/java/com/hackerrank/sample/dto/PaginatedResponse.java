package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Envoltorio estándar para respuestas con paginación")
public record PaginatedResponse<T>(
        @Schema(description = "Lista de elementos de la página actual")
        List<T> content,

        @Schema(description = "Cantidad total de registros existentes en la base de datos", example = "500")
        long totalElements,

        @Schema(description = "Cantidad total de páginas disponibles según el tamaño solicitado", example = "50")
        int totalPages,

        @Schema(description = "Índice de la página actual (basado en 0)", example = "0")
        int pageNumber,

        @Schema(description = "Cantidad de elementos por página", example = "10")
        int pageSize
) {
}