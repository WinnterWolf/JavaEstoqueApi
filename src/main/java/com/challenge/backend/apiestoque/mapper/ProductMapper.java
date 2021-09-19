package com.challenge.backend.apiestoque.mapper;

import com.challenge.backend.apiestoque.dto.request.ProductDTO;
import com.challenge.backend.apiestoque.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    Product toModel(ProductDTO productDTO);

    ProductDTO toDTO(Product product);
}
