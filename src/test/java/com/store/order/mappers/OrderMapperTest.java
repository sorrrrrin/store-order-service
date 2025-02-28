package com.store.order.mappers;

import com.store.order.utils.TestConstants;
import com.store.order.utils.TestUtils;
import com.store.order.dtos.*;
import com.store.order.entities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    private final OrderMapper orderMapper = new OrderMapperImpl();

    @Test
    void orderToOrderDtoTest() {
        Order order = TestUtils.getOrder();

        OrderDto orderDto = orderMapper.orderToOrderDto(order);

        assertNotNull(orderDto);
        assertEquals(TestConstants.ORDER_ID, orderDto.getId());
        assertEquals(TestConstants.ORDER_STATUS, orderDto.getStatus());
    }

    @Test
    void orderToOrderDto_NullOrder_ReturnsNullTest() {
        OrderDto orderDto = orderMapper.orderToOrderDto(null);
        assertNull(orderDto);
    }

    @Test
    void orderDtoToOrderTest() {
        OrderDto orderDto = TestUtils.getOrderDto();

        Order order = orderMapper.orderDtoToOrder(orderDto);

        assertNotNull(order);
        assertEquals(TestConstants.ORDER_ID, order.getId());
        assertEquals(TestConstants.ORDER_STATUS, order.getStatus());
    }

    @Test
    void orderDtoToOrder_NullOrderDto_ReturnsNullTest() {
        Order order = orderMapper.orderDtoToOrder(null);
        assertNull(order);
    }

    @Test
    void customerToCustomerDtoTest() {
        Customer customer = TestUtils.getCustomer();

        CustomerDto customerDto = orderMapper.customerToCustomerDto(customer);

        assertNotNull(customerDto);
        assertEquals(TestConstants.CUSTOMER_ID, customerDto.getId());
        assertEquals(TestConstants.CUSTOMER_EMAIL, customerDto.getEmail());
    }

    @Test
    void customerDtoToCustomerTest() {
        CustomerDto customerDto = TestUtils.getCustomerDto();

        Customer customer = orderMapper.customerDtoToCustomer(customerDto);

        assertNotNull(customer);
        assertEquals(TestConstants.CUSTOMER_ID, customer.getId());
        assertEquals(TestConstants.CUSTOMER_EMAIL, customer.getEmail());
    }

    @Test
    void addressToAddressDtoTest() {
        Address address = TestUtils.getAddress();

        AddressDto addressDto = orderMapper.addressToAddressDto(address);

        assertNotNull(addressDto);
        assertEquals(TestConstants.ADDRESS_STREET, addressDto.getStreet());
    }

    @Test
    void addressDtoToAddressTest() {
        AddressDto addressDto = TestUtils.getAddressDto();

        Address address = orderMapper.AddressDtoToAddress(addressDto);

        assertNotNull(address);
        assertEquals(TestConstants.ADDRESS_STREET, address.getStreet());
    }

    @Test
    void orderItemDtosToOrderItemsTest() {
        List<OrderItemDto> orderItemDtos = List.of(TestUtils.getOrderItemDto());

        List<OrderItem> orderItems = orderMapper.orderItemDtosToOrderItems(orderItemDtos);

        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        assertEquals(TestConstants.ORDER_ITEM_ID, orderItems.get(0).getId());
    }

    @Test
    void orderItemsToOrderItemDtosTest() {
        List<OrderItem> orderItems = List.of(TestUtils.getOrderItem());

        List<OrderItemDto> orderItemDtos = orderMapper.orderItemsToOrderItemDtos(orderItems);

        assertNotNull(orderItemDtos);
        assertEquals(1, orderItemDtos.size());
        assertEquals(TestConstants.ORDER_ITEM_ID, orderItemDtos.get(0).getId());
    }

    @Test
    void productToProductDtoTest() {
        Product product = TestUtils.getProduct();

        ProductDto productDto = orderMapper.productToProductDto(product);

        assertNotNull(productDto);
        assertEquals(TestConstants.PRODUCT_ID, productDto.getId());
        assertEquals(TestConstants.PRODUCT_SKU, productDto.getSku());
    }

    @Test
    void productDtoToProductTest() {
        ProductDto productDto = TestUtils.getProductDto();

        Product product = orderMapper.productDtoToProduct(productDto);

        assertNotNull(product);
        assertEquals(TestConstants.PRODUCT_ID, product.getId());
        assertEquals(TestConstants.PRODUCT_SKU, product.getSku());
    }

}
