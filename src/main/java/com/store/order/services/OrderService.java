package com.store.order.services;

import com.store.order.dtos.OrderDto;
import com.store.order.entities.Customer;
import com.store.order.entities.Order;
import com.store.order.entities.OrderItem;
import com.store.order.exceptions.CustomerNotFoundException;
import com.store.order.exceptions.OrderNotFoundException;
import com.store.order.exceptions.ProductNotFoundException;
import com.store.order.mappers.OrderMapper;
import com.store.order.repositories.CustomerRepository;
import com.store.order.repositories.OrderRepository;
import com.store.order.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {
    @Value("${spring.kafka.topic}")
    private String topic;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::orderToOrderDto).collect(Collectors.toList());
    }

    public OrderDto getOrderById(String id) {
        return orderMapper.orderToOrderDto(orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found")));
    }

    public OrderDto addOrder(OrderDto orderDto) {
        Order order = Order.builder().build();

        Customer customer = customerRepository.findById(orderDto.getCustomer().getId()).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + orderDto.getCustomer().getId() + " not found"));

        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(orderDto.getStatus());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setShippingAddress(orderMapper.AddressDtoToAddress(orderDto.getShippingAddress()));
        order.setTotalAmount(
                orderDto.getOrderItems().stream().mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity()).sum());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = orderDto.getOrderItems().stream().
                map(orderItemDto -> {
                    return OrderItem.builder()
                            .product(productRepository.findById(orderItemDto.getProduct().getId())
                                    .orElseThrow(()->new ProductNotFoundException("Product with id " + orderItemDto.getProduct().getId() + " not found")))
                            .quantity(orderItemDto.getQuantity())
                            .price(orderItemDto.getPrice())
                            .order(savedOrder)
                            .build();
                }).collect(Collectors.toList());

        savedOrder.setOrderItems(orderItems);

        return orderMapper.orderToOrderDto(orderRepository.save(savedOrder));
    }

    public OrderDto updateOrder(String id, OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));

        existingOrder.setId(orderDto.getId());
        existingOrder.setCustomer(orderMapper.customerDtoToCustomer(orderDto.getCustomer()));
        existingOrder.setStatus(orderDto.getStatus());
        existingOrder.setCreatedAt(orderDto.getCreatedAt());
        existingOrder.setPaymentMethod(orderDto.getPaymentMethod());
        existingOrder.setTotalAmount(orderDto.getTotalAmount());
        existingOrder.setShippingAddress(orderMapper.AddressDtoToAddress(orderDto.getShippingAddress()));
        existingOrder.setOrderItems(orderMapper.orderItemDtosToOrderItems(orderDto.getOrderItems()));

//        kafkaTemplate.send(topic, orderDto.toString());

        return orderMapper.orderToOrderDto(orderRepository.save(existingOrder));
    }

    public void deleteOrder(OrderDto orderDto) {
        orderRepository.delete(orderMapper.orderDtoToOrder(orderDto));
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

}
