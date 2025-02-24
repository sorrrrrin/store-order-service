package com.store.order.controllers;

import com.store.order.dtos.OrderDto;
import com.store.order.dtos.ProductDto;
import com.store.order.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "Manage Orders")
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "Get all orders", description = "Retrieve all orders from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)))
    })
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getOrders() {
        List<OrderDto> allOrders = orderService.getAllOrders();
        return ResponseEntity.ok(allOrders);
    }

    @Operation(summary = "Add a new order", description = "Create a new order with customer and products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/orders")
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) {
        OrderDto savedOrder = orderService.addOrder(orderDto);
        return ResponseEntity.ok(savedOrder);
    }

    @Operation(summary = "Update an existing order", description = "Modify an order with the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/orders")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) {
        OrderDto savedOrder = orderService.updateOrder(orderDto.getId(), orderDto);
        return ResponseEntity.ok(savedOrder);
    }

    @Operation(summary = "Delete an order", description = "Remove an order from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/orders")
    public ResponseEntity<OrderDto> deleteOrder(@RequestBody OrderDto orderDto) {
        orderService.deleteOrder(orderDto);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Delete all orders", description = "Remove all orders from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All orders deleted successfully")
    })
    @DeleteMapping("/orders/all")
    public ResponseEntity<ProductDto> deleteAllOrders() {
        orderService.deleteAllOrders();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDto);
    }
}