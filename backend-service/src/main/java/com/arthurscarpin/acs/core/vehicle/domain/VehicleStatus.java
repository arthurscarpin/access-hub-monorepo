package com.arthurscarpin.acs.core.vehicle.domain;

public enum VehicleStatus {
    ACTIVE,
    BLOCKED;

    public VehicleStatus toggle() {
        return this == ACTIVE ? BLOCKED : ACTIVE;
    }
}
