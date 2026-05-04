package com.arthurscarpin.acs.core.owner.domain;

import com.arthurscarpin.acs.core.owner.exception.DocumentInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RGTest {

    @Test
    @DisplayName("Given valid RG, when creating, then should accept value")
    void shouldCreateValidRg() {
        RG rg = new RG("1234567");

        assertEquals("1234567", rg.value());
    }

    @Test
    @DisplayName("Given formatted RG, when creating, then should normalize value")
    void shouldNormalizeRg() {
        RG rg = new RG("123.456.7-8");

        assertEquals("12345678", rg.value());
    }

    @Test
    @DisplayName("Given blank RG, when creating, then should throw exception")
    void shouldThrowWhenBlank() {
        assertThrows(DocumentInvalidException.class,
                () -> new RG(" "));
    }

    @Test
    @DisplayName("Given null RG, when creating, then should throw exception")
    void shouldThrowWhenNull() {
        assertThrows(DocumentInvalidException.class,
                () -> new RG(null));
    }

    @Test
    @DisplayName("Given RG with less than 7 digits, when creating, then should throw exception")
    void shouldThrowWhenTooShort() {
        assertThrows(DocumentInvalidException.class,
                () -> new RG("123456"));
    }

    @Test
    @DisplayName("Given RG with more than 10 digits, when creating, then should throw exception")
    void shouldThrowWhenTooLong() {
        assertThrows(DocumentInvalidException.class,
                () -> new RG("12345678901"));
    }
}