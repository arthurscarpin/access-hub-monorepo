package com.arthurscarpin.acs.core.vehicle.domain;

import com.arthurscarpin.acs.core.vehicle.exception.PlateInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlateTest {

    @Test
    @DisplayName("Given valid raw plate, when creating Plate, then should normalize correctly")
    void shouldNormalizePlate() {
        Plate plate = new Plate("abc-1234");

        assertEquals("ABC1234", plate.plate());
    }

    @Test
    @DisplayName("Given lowercase plate, when creating Plate, then should convert to uppercase")
    void shouldConvertToUppercase() {
        Plate plate = new Plate("abc1234");

        assertEquals("ABC1234", plate.plate());
    }

    @Test
    @DisplayName("Given plate shorter than 7 characters, when creating Plate, then should throw PlateInvalidException")
    void shouldThrowExceptionWhenPlateIsTooShort() {
        assertThrows(PlateInvalidException.class,
                () -> new Plate("abc123"));
    }

    @Test
    @DisplayName("Given plate longer than 7 characters, when creating Plate, then should throw PlateInvalidException")
    void shouldThrowExceptionWhenPlateIsTooLong() {
        assertThrows(PlateInvalidException.class,
                () -> new Plate("abc123456"));
    }

    @Test
    @DisplayName("Given valid normalized plate, when creating Plate, then should accept it")
    void shouldAcceptValidPlate() {
        Plate plate = new Plate("abc1234");

        assertEquals("ABC1234", plate.plate());
    }
}