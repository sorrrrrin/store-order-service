package com.store.order.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.order.utils.TestUtils;
import com.store.order.config.TestSecurityConfig;
import com.store.order.dtos.OrderDto;
import com.store.order.services.OrderService;
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

@WebMvcTest(OrderController.class)
@Import(TestSecurityConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        orderDto = TestUtils.getOrderDto();
    }

    @Test
    void getOrdersTest() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(orderDto.getId()));
    }

    @Test
    void addOrderTest() throws Exception {
        when(orderService.addOrder(any(OrderDto.class))).thenReturn(orderDto);

        String orderDtoJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(post("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderDtoJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.getId()));
    }

    @Test
    void updateOrderTest() throws Exception {
        when(orderService.updateOrder(eq(orderDto.getId()), any(OrderDto.class))).thenReturn(orderDto);

        String orderDtoJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(put("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.getId()));
    }

    @Test
    void deleteOrderTest() throws Exception {
        doNothing().when(orderService).deleteOrder(any(OrderDto.class));

        String orderDtoJson = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(delete("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAllOrdersTest() throws Exception {
        doNothing().when(orderService).deleteAllOrders();

        mockMvc.perform(delete("/api/order/orders/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderByIdTest() throws Exception {
        when(orderService.getOrderById(orderDto.getId())).thenReturn(orderDto);

        mockMvc.perform(get("/api/order/orders/{id}", orderDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.getId()));
    }
}