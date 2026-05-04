package com.arthurscarpin.acs.core.owner.domain;

import com.arthurscarpin.acs.core.owner.exception.NameInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    @DisplayName("Given null name, when creating Name, then should throw NameInvalidException")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(NameInvalidException.class, () -> new Name(null));
    }

    @Test
    @DisplayName("Given blank name, when creating Name, then should throw NameInvalidException")
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(NameInvalidException.class, () -> new Name("   "));
    }

    @Test
    @DisplayName("Given valid name, when creating Name, then should trim spaces and normalize casing")
    void shouldNormalizeNameRemovingExtraSpacesAndLowercasing() {
        Name name = new Name("   joao    silva   ");

        assertEquals("Joao Silva", name.name());
    }

    @Test
    @DisplayName("Given lowercase name, when creating Name, then should capitalize each word")
    void shouldCapitalizeEachWord() {
        Name name = new Name("maria fernanda");

        assertEquals("Maria Fernanda", name.name());
    }

    @Test
    @DisplayName("Given name with multiple spaces, when creating Name, then should collapse spaces")
    void shouldCollapseMultipleSpaces() {
        Name name = new Name("ana     clara   souza");

        assertEquals("Ana Clara Souza", name.name());
    }

    @Test
    @DisplayName("Given name with accents, when creating Name, then should preserve characters and normalize casing")
    void shouldPreserveAccentsAndNormalizeCasing() {
        Name name = new Name("joão   álvaro");

        assertEquals("João Álvaro", name.name());
    }
}