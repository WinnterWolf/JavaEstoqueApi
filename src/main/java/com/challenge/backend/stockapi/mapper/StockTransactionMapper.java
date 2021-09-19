package com.challenge.backend.stockapi.mapper;

import com.challenge.backend.stockapi.dto.request.StockTransactionDTO;
import com.challenge.backend.stockapi.entity.StockTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockTransactionMapper {

    StockTransactionMapper INSTANCE = Mappers.getMapper( StockTransactionMapper.class) ;

    @Mapping(target = "transactionDate", source = "transactionDate", dateFormat = "dd-MM-yyyy")
    StockTransaction toModel(StockTransactionDTO stockTransactionDTO);

    StockTransactionDTO toDTO(StockTransaction stockTransaction);
}
