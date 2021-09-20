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
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransactionDTO {


    private long id;

    @NotNull
    private int product;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType transactionType;

    @NotNull
    private BigDecimal value;

    private LocalDateTime transactionDate;

    @NotNull
    private int amount;
}
