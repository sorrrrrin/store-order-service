package com.store.order.controllers;

import com.store.order.dtos.ProductDTO;
import com.store.order.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CatalogController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.addProduct(productDTO);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/products")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.updateProduct(productDTO.getId(), productDTO);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/products")
    public ResponseEntity<ProductDTO> deleteProduct(@RequestBody ProductDTO productDTO) {
        productService.deleteProduct(productDTO);
        return ResponseEntity.ok().build();
    }
}