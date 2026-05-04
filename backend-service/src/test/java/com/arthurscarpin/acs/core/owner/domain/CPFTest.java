package com.arthurscarpin.acs.core.owner.domain;

import com.arthurscarpin.acs.core.owner.exception.DocumentInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPFTest {

    @Test
    @DisplayName("Given valid CPF, when creating, then should accept value")
    void shouldAcceptValidCpf() {
        CPF cpf = new CPF("12345678901");

        assertEquals("12345678901", cpf.value());
    }

    @Test
    @DisplayName("Given formatted CPF, when creating, then should normalize value")
    void shouldNormalizeCpf() {
        CPF cpf = new CPF("123.456.789-01");

        assertEquals("12345678901", cpf.value());
    }

    @Test
    @DisplayName("Given blank CPF, when creating, then should throw exception")
    void shouldThrowWhenBlank() {
        assertThrows(DocumentInvalidException.class,
                () -> new CPF(" "));
    }

    @Test
    @DisplayName("Given CPF with less than 11 digits, when creating, then should throw exception")
    void shouldThrowWhenLessThan11Digits() {
        assertThrows(DocumentInvalidException.class, () -> new CPF("1234567890"));
    }

    @Test
    @DisplayName("Given CPF with more than 11 digits, when creating, then should throw exception")
    void shouldThrowWhenMoreThan11Digits() {
        assertThrows(DocumentInvalidException.class, () -> new CPF("123456789012"));
    }
}