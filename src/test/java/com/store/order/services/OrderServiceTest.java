package com.store.order.services;

import com.store.order.utils.TestConstants;
import com.store.order.utils.TestUtils;
import com.store.order.dtos.OrderDto;
import com.store.order.dtos.OrderItemDto;
import com.store.order.dtos.ProductDto;
import com.store.order.entities.Customer;
import com.store.order.entities.Order;
import com.store.order.exceptions.CustomerNotFoundException;
import com.store.order.exceptions.OrderNotFoundException;
import com.store.order.exceptions.ProductNotFoundException;
import com.store.order.mappers.OrderMapper;
import com.store.order.mappers.OrderMapperImpl;
import com.store.order.repositories.CustomerRepository;
import com.store.order.repositories.OrderRepository;
import com.store.order.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private final OrderMapper orderMapper = new OrderMapperImpl();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderMapper, orderRepository, productRepository, customerRepository, kafkaTemplate);
    }

    @Test
    void getAllOrdersTest() {
        Order order = TestUtils.getOrder();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDto> allOrders = orderService.getAllOrders();

        assertNotNull(allOrders);
        assertEquals(1, allOrders.size());
        assertEquals(TestConstants.ORDER_ID, allOrders.get(0).getId());
        assertEquals(TestConstants.ORDER_STATUS, allOrders.get(0).getStatus());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderByIdThrowsExceptionWhenNotFound() {
        String invalidOrderId = "invalid-id";
        when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(invalidOrderId));

        verify(orderRepository, times(1)).findById(invalidOrderId);
    }

    @Test
    void deleteOrderTest() {
        OrderDto orderDto = TestUtils.getOrderDto();

        orderService.deleteOrder(orderDto);

        verify(orderRepository, times(1)).delete(any(Order.class));
    }

    @Test
    void deleteAllOrdersTest() {
        orderService.deleteAllOrders();

        verify(orderRepository, times(1)).deleteAll();
    }

    @Test
    void updateOrderTest() {
        OrderDto orderDto = TestUtils.getOrderDto();
        Order existingOrder = TestUtils.getOrder();

        when(orderRepository.findById(orderDto.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        OrderDto updatedOrder = orderService.updateOrder(orderDto.getId(), orderDto);

        assertNotNull(updatedOrder);
        assertEquals(orderDto.getId(), updatedOrder.getId());
        assertEquals(orderDto.getStatus(), updatedOrder.getStatus());
        verify(orderRepository, times(1)).findById(orderDto.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void addOrderTest() {
        Customer customer = TestUtils.getCustomer();

        OrderDto orderDto = TestUtils.getOrderDto();
        ProductDto productDto = TestUtils.getProductDto();
        OrderItemDto orderItemDto = TestUtils.getOrderItemDto();
        orderItemDto.setQuantity(2);
        orderItemDto.setPrice(2);
        orderItemDto.setProduct(productDto);
        orderDto.setOrderItems(List.of(TestUtils.getOrderItemDto(), orderItemDto));

        Order order = TestUtils.getOrder();

        when(customerRepository.findById(orderDto.getCustomer().getId())).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productRepository.findById(anyString())).thenReturn(Optional.of(TestUtils.getProduct()));

        OrderDto savedOrder = orderService.addOrder(orderDto);

        assertNotNull(savedOrder);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(2)).save(orderCaptor.capture());
        List<Order> capturedOrders = orderCaptor.getAllValues();

        assertEquals(2, capturedOrders.size());
        Order firstSavedOrder = capturedOrders.get(0);
        Order secondSavedOrder = capturedOrders.get(1);

        assertEquals(orderDto.getCustomer().getId(), firstSavedOrder.getCustomer().getId());
        assertEquals(orderDto.getStatus(), firstSavedOrder.getStatus());
        assertEquals(5, firstSavedOrder.getTotalAmount());

        assertEquals(2, secondSavedOrder.getOrderItems().size());

        verify(customerRepository, times(1)).findById(orderDto.getCustomer().getId());
        verify(productRepository, times(orderDto.getOrderItems().size())).findById(anyString());

    }

    @Test
    void addOrderThrowsProductNotFoundExceptionTest() {
        Customer customer = TestUtils.getCustomer();
        OrderDto orderDto = TestUtils.getOrderDto();
        ProductDto productDto = TestUtils.getProductDto();
        OrderItemDto orderItemDto = TestUtils.getOrderItemDto();
        orderItemDto.setQuantity(2);
        orderItemDto.setPrice(2);
        orderItemDto.setProduct(productDto);
        orderDto.setOrderItems(List.of(TestUtils.getOrderItemDto(), orderItemDto));

        when(customerRepository.findById(orderDto.getCustomer().getId())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.addOrder(orderDto));

        verify(customerRepository, times(1)).findById(orderDto.getCustomer().getId());
        verify(productRepository, times(1)).findById(anyString());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void addOrderThrowsCustomerNotFoundExceptionTest() {
        OrderDto orderDto = TestUtils.getOrderDto();
        ProductDto productDto = TestUtils.getProductDto();
        OrderItemDto orderItemDto = TestUtils.getOrderItemDto();
        orderItemDto.setQuantity(2);
        orderItemDto.setPrice(2);
        orderItemDto.setProduct(productDto);
        orderDto.setOrderItems(List.of(TestUtils.getOrderItemDto(), orderItemDto));

        when(customerRepository.findById(orderDto.getCustomer().getId())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> orderService.addOrder(orderDto));

        verify(customerRepository, times(1)).findById(orderDto.getCustomer().getId());
        verify(productRepository, never()).findById(anyString());
        verify(orderRepository, never()).save(any(Order.class));
    }

}
