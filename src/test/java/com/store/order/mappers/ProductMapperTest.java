package com.store.order.mappers;

import com.store.order.utils.TestConstants;
import com.store.order.utils.TestUtils;
import com.store.order.dtos.ProductDto;
import com.store.order.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ProductMapperTest {

    private final ProductMapper productMapper = new ProductMapperImpl();

    @Test
    void productToProductDtoTest() {
        Product product = TestUtils.getProduct();

        ProductDto productDto = productMapper.productToProductDto(product);

        assertNotNull(productDto);
        assertEquals(TestConstants.PRODUCT_ID, productDto.getId());
        assertEquals(TestConstants.PRODUCT_SKU, productDto.getSku());
        assertEquals(product.getPrice(), productDto.getPrice());
    }

    @Test
    void productToProductDto_NullProduct_ReturnsNullTest() {
        ProductDto productDto = productMapper.productToProductDto(null);
        assertNull(productDto);
    }

    @Test
    void productDtoToProductTest() {
        ProductDto productDto = TestUtils.getProductDto();

        Product product = productMapper.productDtoToProduct(productDto);

        assertNotNull(product);
        assertEquals(TestConstants.PRODUCT_ID, product.getId());
        assertEquals(TestConstants.PRODUCT_SKU, product.getSku());
        assertEquals(productDto.getPrice(), product.getPrice());
    }

    @Test
    void productDtoToProduct_NullProductDto_ReturnsNullTest() {
        Product product = productMapper.productDtoToProduct(null);
        assertNull(product);
    }
}