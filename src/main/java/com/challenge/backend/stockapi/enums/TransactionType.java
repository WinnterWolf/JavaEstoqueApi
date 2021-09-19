package com.challenge.backend.stockapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

    INCOME("Entrada"),
    OUTCOME("Saída");

    private final String description;
}
