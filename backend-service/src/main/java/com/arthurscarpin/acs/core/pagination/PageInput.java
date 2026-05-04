package com.arthurscarpin.acs.core.pagination;

import java.time.Instant;

public record PageInput(
        int pageNumber,
        int pageSize,
        String plate,
        Instant from,
        Instant to
) {
}
