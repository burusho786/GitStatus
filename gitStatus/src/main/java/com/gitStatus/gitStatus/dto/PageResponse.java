// src/main/java/com/gitStatus/gitStatus/dto/PageResponse.java
package com.gitStatus.gitStatus.dto;

import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

/**
 * Stable paginated envelope for the frontend.
 * Shape: { items, page, size, total, generatedAt }
 */
public record PageResponse<T>(
        List<T> items,
        int page,
        int size,
        long total,
        Instant generatedAt
) {

    public static <E, T> PageResponse<T> of(Page<E> page, Function<E, T> mapper) {
        return new PageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                Instant.now()
        );
    }
}