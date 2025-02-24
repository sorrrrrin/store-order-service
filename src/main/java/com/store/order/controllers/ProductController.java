package com.store.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.store.order.dtos.ProductDto;
import com.store.order.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    @Operation(summary = "Add a new product", description = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully")
    })
    @PostMapping("/products")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.addProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "Update a product", description = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/products")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) throws JsonProcessingException {
        ProductDto savedProduct = productService.updateProduct(productDto.getId(), productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @Operation(summary = "Delete a product", description = "Remove a product from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/products")
    public ResponseEntity<ProductDto> deleteProduct(@RequestBody ProductDto productDto) {
        productService.deleteProduct(productDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete all products", description = "Remove all products from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All products deleted successfully")
    })
    @DeleteMapping("/products/all")
    public ResponseEntity<ProductDto> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }
}