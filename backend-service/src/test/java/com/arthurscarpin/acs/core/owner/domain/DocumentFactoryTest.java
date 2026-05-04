package com.arthurscarpin.acs.core.owner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentFactoryTest {

    @Test
    @DisplayName("Given document type CPF, when creating document, then should return CPF instance")
    void shouldCreateCpf() {
        Document document = DocumentFactory.create(DocumentType.CPF, "12345678901");

        assertInstanceOf(CPF.class, document);
        assertEquals("12345678901", document.value());
    }

    @Test
    @DisplayName("Given document type RG, when creating document, then should return RG instance")
    void shouldCreateRg() {
        Document document = DocumentFactory.create(DocumentType.RG, "12345678");

        assertInstanceOf(RG.class, document);
        assertEquals("12345678", document.value());
    }

    @Test
    @DisplayName("Given CPF input with formatting, when creating document, then should normalize and return CPF")
    void shouldNormalizeCpfThroughFactory() {
        Document document = DocumentFactory.create(DocumentType.CPF, "123.456.789-01");

        assertTrue(document instanceof CPF);
        assertEquals("12345678901", document.value());
    }

    @Test
    @DisplayName("Given RG input with formatting, when creating document, then should normalize and return RG")
    void shouldNormalizeRgThroughFactory() {
        Document document = DocumentFactory.create(DocumentType.RG, "12.345.67-8");

        assertTrue(document instanceof RG);
        assertEquals("12345678", document.value());
    }
}