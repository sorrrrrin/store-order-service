package com.store.order.mappers;

import com.store.order.dtos.*;
import com.store.order.entities.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto orderToOrderDto(Order order);

    Order orderDtoToOrder(OrderDto orderDto);

    CustomerDto customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDto customerDto);

    AddressDto addressToAddressDto(Address address);

    Address AddressDtoToAddress(AddressDto addressDto);

    List<OrderItem> orderItemDtosToOrderItems(List<OrderItemDto> orderItemDtos);

    List<OrderItemDto> orderItemsToOrderItemDtos(List<OrderItem> orderItems);

    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);

}