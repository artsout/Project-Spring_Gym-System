package com.Gym.Dto.Page;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
   
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getPageable().getPageSize(),
                page.getPageable().getPageNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}