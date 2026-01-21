package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Standard wrapper for paginated API responses")
public record PaginatedResponse<T>(
        @Schema(description = "List of items in the current page")
        List<T> content,

        @Schema(description = "Total number of records in the database", example = "500")
        long totalElements,

        @Schema(description = "Total number of pages based on page size", example = "50")
        int totalPages,

        @Schema(description = "Current page index (zero-based)", example = "0")
        int pageNumber,

        @Schema(description = "Number of elements per page", example = "10")
        int pageSize
) {
}