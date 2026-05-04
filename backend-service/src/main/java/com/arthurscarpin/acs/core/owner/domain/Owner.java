package com.arthurscarpin.acs.core.owner.domain;

import java.util.UUID;

public record Owner(
        UUID id,
        String name,
        String document,
        DocumentType documentType,
        String email
) {
}
