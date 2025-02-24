package com.store.order.controllers;

import com.store.order.dtos.OrderDto;
import com.store.order.dtos.ProductDto;
import com.store.order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getOrders() {
        List<OrderDto> allOrders = orderService.getAllOrders();
        return ResponseEntity.ok(allOrders);
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) {
        OrderDto savedOrder = orderService.addOrder(orderDto);
        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/orders")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) {
        OrderDto savedOrder = orderService.updateOrder(orderDto.getId(), orderDto);
        return ResponseEntity.ok(savedOrder);
    }

    @DeleteMapping("/orders")
    public ResponseEntity<OrderDto> deleteOrder(@RequestBody OrderDto orderDto) {
        orderService.deleteOrder(orderDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/orders/all")
    public ResponseEntity<ProductDto> deleteAllProducts() {
        orderService.deleteAllOrders();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDto);
    }
}