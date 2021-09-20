package com.challenge.backend.stockapi.entity;

import lombok.Data;

@Data
public class ProductQuantity {

    private Product product;

    private int stockOutcome;

}
