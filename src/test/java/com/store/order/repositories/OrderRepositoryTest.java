package com.store.order.repositories;

import com.store.order.entities.Customer;
import com.store.order.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");

        customer = customerRepository.save(customer);

        Order order1 = new Order();
        order1.setCustomer(customer);

        Order order2 = new Order();
        order2.setCustomer(customer);

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @Test
    void testFindOrdersByCustomerId() {
        List<Order> orders = orderRepository.findOrdersByCustomerId(customer.getId());
        assertThat(orders).hasSize(2);
    }
}