package com.arthurscarpin.acs.core.pagination;

import java.util.List;

public record PageOutput<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
