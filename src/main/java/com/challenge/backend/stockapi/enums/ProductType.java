package com.challenge.backend.stockapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductType {

    ELECTRONIC("Eletrônico"),
    APPLIANCE("Eletrodoméstio"),
    FURNITURE("Móvel");

    private final String description;
}
