package com.store.order.mappers;

import com.store.order.dtos.ProductDTO;
import com.store.order.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO productToProductDto(Product product);

    Product productDtoToProduct(ProductDTO productDto);
}