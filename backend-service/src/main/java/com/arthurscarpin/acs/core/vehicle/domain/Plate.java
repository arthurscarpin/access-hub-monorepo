package com.arthurscarpin.acs.core.vehicle.domain;

import com.arthurscarpin.acs.core.vehicle.exception.PlateInvalidException;

public record Plate(
        String plate
) {
    public Plate {
        plate = normalized(plate);
        validate(plate);
    }

    private static String normalized(String plate) {
        return plate.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }

    private static void validate(String plateNormalized) {
        if (plateNormalized.length() != 7) {
            throw new PlateInvalidException("License plate must have 7 characters");
        }
    }
}
