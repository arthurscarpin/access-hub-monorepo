package com.arthurscarpin.acs.core.accessevent.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AccessEvent(
        UUID id,
        String plate,
        OffsetDateTime timestamp,
        Direction direction,
        AccessResult result
) {

    public static AccessEvent create(String plate, OffsetDateTime timestamp, Direction direction, AccessResult result) {
        return new AccessEvent(null, plate, timestamp, direction, result);
    }
}
