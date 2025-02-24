package com.store.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.store.order.dtos.CustomerDto;
import com.store.order.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        List<CustomerDto> allCustomers = customerService.getAllCustomers();
        return ResponseEntity.ok(allCustomers);
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.addCustomer(customerDto);
        return ResponseEntity.ok(savedCustomer);
    }

    @PutMapping("/customers")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto) throws JsonProcessingException {
        CustomerDto savedCustomer = customerService.updateCustomer(customerDto.getId(), customerDto);
        return ResponseEntity.ok(savedCustomer);
    }

    @DeleteMapping("/customers")
    public ResponseEntity<CustomerDto> deleteCustomer(@RequestBody CustomerDto customerDto) {
        customerService.deleteCustomer(customerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/customers/all")
    public ResponseEntity<CustomerDto> deleteAllCustomers() {
        customerService.deleteAllCustomers();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }
}