package com.store.order.services;

import com.store.order.dtos.CustomerDto;
import com.store.order.entities.Customer;
import com.store.order.exceptions.CustomerNotFoundException;
import com.store.order.mappers.OrderMapper;
import com.store.order.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {
    @Value("${spring.kafka.topic}")
    private String topic;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(orderMapper::customerToCustomerDto).collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(String id) {
        return orderMapper.customerToCustomerDto(customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found")));
    }

    public CustomerDto addCustomer(CustomerDto customerDto) {
        return orderMapper.customerToCustomerDto(customerRepository.save(orderMapper.customerDtoToCustomer(customerDto)));
    }

    public CustomerDto updateCustomer(String id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        existingCustomer.setName(customerDto.getName());

//        kafkaTemplate.send(topic, customerDto.toString());

        return orderMapper.customerToCustomerDto(customerRepository.save(existingCustomer));
    }

    public void deleteCustomer(CustomerDto customerDto) {
        customerRepository.delete(orderMapper.customerDtoToCustomer(customerDto));
    }

    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

}
