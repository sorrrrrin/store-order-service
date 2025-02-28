package com.store.order.utils;

import com.store.order.commons.OrderStatus;
import com.store.order.dtos.*;
import com.store.order.entities.*;

import java.util.List;

public class TestUtils {
    public static Order getOrder() {
        return Order.builder()
                .id(TestConstants.ORDER_ID)
                .status(OrderStatus.PENDING)
                .customer(getCustomer())
                .orderItems(List.of(getOrderItem()))
                .status(TestConstants.ORDER_STATUS)
                .build();
    }

    public static OrderDto getOrderDto() {
        return OrderDto.builder()
                .id(TestConstants.ORDER_ID)
                .customer(getCustomerDto())
                .status(TestConstants.ORDER_STATUS)
                .build();
    }

    public static Customer getCustomer() {
        return Customer.builder()
                .id(TestConstants.CUSTOMER_ID)
                .email(TestConstants.CUSTOMER_EMAIL)
                .build();
    }

    public static CustomerDto getCustomerDto() {
        return CustomerDto.builder()
                .id(TestConstants.CUSTOMER_ID)
                .email(TestConstants.CUSTOMER_EMAIL)
                .build();
    }

    public static Product getProduct() {
        return Product.builder()
                .id(TestConstants.PRODUCT_ID)
                .sku(TestConstants.PRODUCT_SKU)
                .price(1)
                .build();
    }

    public static ProductDto getProductDto() {
        return ProductDto.builder()
                .id(TestConstants.PRODUCT_ID)
                .sku(TestConstants.PRODUCT_SKU)
                .price(1)
                .build();
    }

    public static OrderItem getOrderItem() {
        return OrderItem.builder()
                .id(TestConstants.ORDER_ITEM_ID)
                .quantity(1)
                .price(1)
                .product(getProduct())
                .build();
    }

    public static OrderItemDto getOrderItemDto() {
        return OrderItemDto.builder()
                .id(TestConstants.ORDER_ITEM_ID)
                .quantity(1)
                .price(1)
                .product(getProductDto())
                .build();
    }

    public static Address getAddress() {
        return Address.builder()
                .street(TestConstants.ADDRESS_STREET)
                .build();
    }

    public static AddressDto getAddressDto() {
        return AddressDto.builder()
                .street(TestConstants.ADDRESS_STREET)
                .build();
    }



}
