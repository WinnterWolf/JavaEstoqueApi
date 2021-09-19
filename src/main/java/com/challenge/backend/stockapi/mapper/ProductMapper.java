package com.challenge.backend.stockapi.mapper;

import com.challenge.backend.stockapi.dto.request.ProductDTO;
import com.challenge.backend.stockapi.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    Product toModel(ProductDTO productDTO);

    ProductDTO toDTO(Product product);
}
