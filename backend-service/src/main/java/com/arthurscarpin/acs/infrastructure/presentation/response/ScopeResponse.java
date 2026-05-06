package com.arthurscarpin.acs.infrastructure.presentation.response;

import java.util.UUID;

public record ScopeResponse(
        UUID id,
        String name
) {
}
