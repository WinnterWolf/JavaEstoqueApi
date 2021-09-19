package com.challenge.backend.stockapi.dto.request;

import com.challenge.backend.stockapi.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private long id;

    @NotNull
    private int code;

    @NotEmpty
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductType type;

    @NotNull
    private BigDecimal vendorsPrice;

    @NotNull
    private int stockAmount;
}
