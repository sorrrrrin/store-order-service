package com.store.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.store.order.dtos.CustomerDto;
import com.store.order.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        List<CustomerDto> allCustomers = customerService.getAllCustomers();
        return ResponseEntity.ok(allCustomers);
    }

    @Operation(summary = "Add a new customer", description = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer added successfully")
    })
    @PostMapping("/customers")
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.addCustomer(customerDto);
        return ResponseEntity.ok(savedCustomer);
    }

    @Operation(summary = "Update a customer", description = "Update an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/customers")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto) throws JsonProcessingException {
        CustomerDto savedCustomer = customerService.updateCustomer(customerDto.getId(), customerDto);
        return ResponseEntity.ok(savedCustomer);
    }

    @Operation(summary = "Delete a customer", description = "Remove a customer from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/customers")
    public ResponseEntity<CustomerDto> deleteCustomer(@RequestBody CustomerDto customerDto) {
        customerService.deleteCustomer(customerDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete all customers", description = "Remove all customers from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All customers deleted successfully")
    })
    @DeleteMapping("/customers/all")
    public ResponseEntity<CustomerDto> deleteAllCustomers() {
        customerService.deleteAllCustomers();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }
}