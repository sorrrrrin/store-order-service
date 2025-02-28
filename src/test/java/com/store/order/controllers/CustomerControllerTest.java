package com.store.order.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.order.utils.TestUtils;
import com.store.order.config.TestSecurityConfig;
import com.store.order.dtos.CustomerDto;
import com.store.order.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(TestSecurityConfig.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerDto = TestUtils.getCustomerDto();
    }

    @Test
    void getCustomersTest() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Collections.singletonList(customerDto));

        mockMvc.perform(get("/api/order/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(customerDto.getId()));
    }

    @Test
    void addCustomerTest() throws Exception {
        when(customerService.addCustomer(any(CustomerDto.class))).thenReturn(customerDto);

        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(post("/api/order/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerDto.getId()));
    }

    @Test
    void updateCustomerTest() throws Exception {
        when(customerService.updateCustomer(eq(customerDto.getId()), any(CustomerDto.class))).thenReturn(customerDto);

        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(put("/api/order/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerDto.getId()));
    }

    @Test
    void deleteCustomerTest() throws Exception {
        doNothing().when(customerService).deleteCustomer(any(CustomerDto.class));

        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(delete("/api/order/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAllCustomersTest() throws Exception {
        doNothing().when(customerService).deleteAllCustomers();

        mockMvc.perform(delete("/api/order/customers/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerByIdTest() throws Exception {
        when(customerService.getCustomerById(customerDto.getId())).thenReturn(customerDto);

        mockMvc.perform(get("/api/order/customers/{id}", customerDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerDto.getId()));
    }
}