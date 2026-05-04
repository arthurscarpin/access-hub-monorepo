package com.arthurscarpin.acs.core.owner.domain;

import com.arthurscarpin.acs.core.owner.exception.DocumentInvalidException;

public record RG(String value) implements Document {

    public RG {
        if (value == null || value.isBlank()) {
            throw new DocumentInvalidException("RG cannot be empty");
        }

        value = value.replaceAll("\\D", "");

        if (value.length() < 7 || value.length() > 10) {
            throw new DocumentInvalidException("RG must be between 7 and 10 characters");
        }
    }
}