package com.hackerrank.sample.dto;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize
) {}