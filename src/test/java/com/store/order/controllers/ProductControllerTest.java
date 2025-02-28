package com.store.order.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.order.utils.TestUtils;
import com.store.order.config.TestSecurityConfig;
import com.store.order.dtos.ProductDto;
import com.store.order.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(TestSecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productDto = TestUtils.getProductDto();
    }

    @Test
    void getProductsTest() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(productDto));

        mockMvc.perform(get("/api/order/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(productDto.getId()));
    }

    @Test
    void addProductTest() throws Exception {
        when(productService.addProduct(any(ProductDto.class))).thenReturn(productDto);

        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/api/order/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productDto.getId()));
    }

    @Test
    void updateProductTest() throws Exception {
        when(productService.updateProduct(eq(productDto.getId()), any(ProductDto.class))).thenReturn(productDto);

        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(put("/api/order/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productDto.getId()));
    }

    @Test
    void deleteProductTest() throws Exception {
        doNothing().when(productService).deleteProduct(any(ProductDto.class));

        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(delete("/api/order/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAllProductsTest() throws Exception {
        doNothing().when(productService).deleteAllProducts();

        mockMvc.perform(delete("/api/order/products/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductByIdTest() throws Exception {
        when(productService.getProductById(productDto.getId())).thenReturn(productDto);

        mockMvc.perform(get("/api/order/products/{id}", productDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productDto.getId()));
    }
}