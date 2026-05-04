package com.arthurscarpin.acs.core.owner.domain;

import com.arthurscarpin.acs.core.owner.exception.DocumentInvalidException;

public record CPF(String value) implements Document {

    public CPF {
        if (value == null || value.isBlank()) {
            throw new DocumentInvalidException("CPF cannot be null or blank");
        }

        value = value.replaceAll("\\D", "");

        if (value.length() != 11) {
            throw new DocumentInvalidException("CPF must have 11 digits");
        }
    }
}