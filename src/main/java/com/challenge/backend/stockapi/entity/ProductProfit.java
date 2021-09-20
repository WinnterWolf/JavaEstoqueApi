package com.challenge.backend.stockapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProfit {

    private BigDecimal totalProfit;

    private int totalStockOutcome;
}
