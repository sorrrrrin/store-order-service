package com.store.order.mappers;

import com.store.order.dtos.*;
import com.store.order.entities.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);

}