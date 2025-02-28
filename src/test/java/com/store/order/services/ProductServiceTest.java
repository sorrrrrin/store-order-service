package com.store.order.services;

import com.store.order.utils.TestConstants;
import com.store.order.utils.TestUtils;
import com.store.order.dtos.ProductDto;
import com.store.order.entities.Product;
import com.store.order.exceptions.ProductNotFoundException;
import com.store.order.mappers.ProductMapper;
import com.store.order.mappers.ProductMapperImpl;
import com.store.order.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ProductMapper productMapper = new ProductMapperImpl();

    @BeforeEach
    void setUp() {
        productService = new ProductService(TestConstants.TEST_KAFKA_TOPIC, productMapper, productRepository, kafkaTemplate);
    }

    @Test
    void getAllProductsTest() {
        Product product = TestUtils.getProduct();

        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDto> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(TestConstants.PRODUCT_ID, products.get(0).getId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductByIdTest() {
        Product product = TestUtils.getProduct();

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.of(product));

        ProductDto productDto = productService.getProductById(TestConstants.PRODUCT_ID);

        assertNotNull(productDto);
        assertEquals(TestConstants.PRODUCT_ID, productDto.getId());
        verify(productRepository, times(1)).findById(TestConstants.PRODUCT_ID);
    }

    @Test
    void getProductByIdThrowsExceptionWhenNotFound() {
        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(TestConstants.PRODUCT_ID));

        verify(productRepository, times(1)).findById(TestConstants.PRODUCT_ID);
    }

    @Test
    void addProductTest() {
        ProductDto productDto = TestUtils.getProductDto();

        Product product = TestUtils.getProduct();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto savedProduct = productService.addProduct(productDto);

        assertNotNull(savedProduct);
        assertEquals(TestConstants.PRODUCT_ID, savedProduct.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProductTest() {
        ProductDto productDto = TestUtils.getProductDto();
        Product existingProduct = TestUtils.getProduct();

        when(productRepository.findById(TestConstants.PRODUCT_ID)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ProductDto updatedProduct = productService.updateProduct(TestConstants.PRODUCT_ID, productDto);

        assertNotNull(updatedProduct);
        assertEquals(TestConstants.PRODUCT_ID, updatedProduct.getId());
        verify(productRepository, times(1)).findById(TestConstants.PRODUCT_ID);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProductTest() {
        ProductDto productDto = new ProductDto();
        productDto.setId(TestConstants.PRODUCT_ID);

        productService.deleteProduct(productDto);

        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void deleteAllProductsTest() {
        productService.deleteAllProducts();

        verify(productRepository, times(1)).deleteAll();
    }
}