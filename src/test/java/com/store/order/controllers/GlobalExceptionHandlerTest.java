package com.store.order.controllers;

import com.store.order.config.TestSecurityConfig;
import com.store.order.exceptions.OrderNotFoundException;
import com.store.order.services.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(TestSecurityConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void testOrderNotFound() throws Exception {
        UUID orderId = UUID.randomUUID();
        Mockito.when(orderService.getOrderById(orderId.toString()))
                .thenThrow(new OrderNotFoundException("Order with id " + orderId + " not found"));

        mockMvc.perform(get("/api/order/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order with id " + orderId + " not found")
                );
    }

    @Test
    void testGeneralException() throws Exception {
        UUID orderId = UUID.randomUUID();
        Mockito.when(orderService.getOrderById(orderId.toString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/order/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
