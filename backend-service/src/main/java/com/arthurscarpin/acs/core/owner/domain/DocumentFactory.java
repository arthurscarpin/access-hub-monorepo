package com.arthurscarpin.acs.core.owner.domain;

public final class DocumentFactory {

    public static Document create(DocumentType type, String value) {
        return switch (type) {
            case CPF -> new CPF(value);
            case RG -> new RG(value);
        };
    }
}
