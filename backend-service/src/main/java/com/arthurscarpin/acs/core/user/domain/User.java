package com.arthurscarpin.acs.core.user.domain;

import java.util.List;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String email,
        String password,
        Boolean active,
        List<UUID> scopes
) {
}
