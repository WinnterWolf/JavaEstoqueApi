package com.challenge.backend.apiestoque.mapper;

import com.challenge.backend.apiestoque.dto.request.ProductDTO;
import com.challenge.backend.apiestoque.dto.request.StockTransactionDTO;
import com.challenge.backend.apiestoque.entity.Product;
import com.challenge.backend.apiestoque.entity.StockTransaction;
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
