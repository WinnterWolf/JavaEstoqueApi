package com.challenge.backend.stockapi.dto.request;

import com.challenge.backend.stockapi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransactionDTO {


    private long id;

    @NotEmpty
    private int product;

    @Enumerated(EnumType.STRING)
    @NotEmpty
    private TransactionType transactionType;

    @NotBlank
    private BigDecimal value;

    private LocalDate transactionDate;

    @NotEmpty
    private int amount;
}
