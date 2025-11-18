package com.store.order.services;

import com.store.order.utils.TestConstants;
import com.store.order.utils.TestUtils;
import com.store.order.dtos.CustomerDto;
import com.store.order.entities.Customer;
import com.store.order.exceptions.CustomerNotFoundException;
import com.store.order.mappers.OrderMapper;
import com.store.order.mappers.OrderMapperImpl;
import com.store.order.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private final OrderMapper orderMapper = new OrderMapperImpl();

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(orderMapper, customerRepository, kafkaTemplate);
    }

    @Test
    void getAllCustomersTest() {
        Customer customer = TestUtils.getCustomer();

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerDto> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals(TestConstants.CUSTOMER_ID, customers.get(0).getId());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getCustomerByIdTest() {
        Customer customer = TestUtils.getCustomer();

        when(customerRepository.findById(TestConstants.CUSTOMER_ID)).thenReturn(Optional.of(customer));

        CustomerDto customerDto = customerService.getCustomerById(TestConstants.CUSTOMER_ID);

        assertNotNull(customerDto);
        assertEquals(TestConstants.CUSTOMER_ID, customerDto.getId());
        verify(customerRepository, times(1)).findById(TestConstants.CUSTOMER_ID);
    }

    @Test
    void getCustomerByIdThrowsExceptionWhenNotFound() {
        when(customerRepository.findById(TestConstants.CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(TestConstants.CUSTOMER_ID));

        verify(customerRepository, times(1)).findById(TestConstants.CUSTOMER_ID);
    }

    @Test
    void addCustomerTest() {
        CustomerDto customerDto = TestUtils.getCustomerDto();

        Customer customer = TestUtils.getCustomer();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto savedCustomer = customerService.addCustomer(customerDto);

        assertNotNull(savedCustomer);
        assertEquals(TestConstants.CUSTOMER_ID, savedCustomer.getId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomerTest() {
        CustomerDto customerDto = TestUtils.getCustomerDto();

        Customer existingCustomer = TestUtils.getCustomer();

        when(customerRepository.findById(TestConstants.CUSTOMER_ID)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        CustomerDto updatedCustomer = customerService.updateCustomer(TestConstants.CUSTOMER_ID, customerDto);

        assertNotNull(updatedCustomer);
        assertEquals(TestConstants.CUSTOMER_ID, updatedCustomer.getId());
        verify(customerRepository, times(1)).findById(TestConstants.CUSTOMER_ID);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void deleteCustomerTest() {
        CustomerDto customerDto = TestUtils.getCustomerDto();

        customerService.deleteCustomer(customerDto);

        verify(customerRepository, times(1)).delete(any(Customer.class));
    }

    @Test
    void deleteAllCustomersTest() {
        customerService.deleteAllCustomers();

        verify(customerRepository, times(1)).deleteAll();
    }

    @Test
    void getCustomerByEmailTest() {
        Customer customer = TestUtils.getCustomer();

        when(customerRepository.findByEmail(TestConstants.CUSTOMER_EMAIL)).thenReturn(Optional.of(customer));

        CustomerDto customerDto = customerService.getCustomerByEmail(TestConstants.CUSTOMER_EMAIL);

        assertNotNull(customerDto);
        assertEquals(TestConstants.CUSTOMER_ID, customerDto.getId());
        verify(customerRepository, times(1)).findByEmail(TestConstants.CUSTOMER_EMAIL);
    }

    @Test
    void getCustomerByEmailThrowsExceptionTest() {
        when(customerRepository.findByEmail(TestConstants.CUSTOMER_EMAIL)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByEmail(TestConstants.CUSTOMER_EMAIL));

        verify(customerRepository, times(1)).findByEmail(TestConstants.CUSTOMER_EMAIL);
    }
}