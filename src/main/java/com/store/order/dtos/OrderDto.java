package com.store.order.dtos;

import com.store.order.commons.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private String id;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private double totalAmount;
    private String paymentMethod;
    private CustomerDto customer;
    private AddressDto shippingAddress;
    private List<OrderItemDto> orderItems;
}