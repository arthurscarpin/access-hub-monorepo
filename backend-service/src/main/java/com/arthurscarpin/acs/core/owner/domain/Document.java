package com.arthurscarpin.acs.core.owner.domain;

public sealed interface Document permits CPF, RG {
    String value();
}
