package com.store.order.services;

import com.store.order.dtos.ProductDto;
import com.store.order.entities.Product;
import com.store.order.exceptions.ProductNotFoundException;
import com.store.order.mappers.ProductMapper;
import com.store.order.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Value("${spring.kafka.topic}")
    private String topic;

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProductService(ProductMapper productMapper, ProductRepository productRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::productToProductDto).collect(Collectors.toList());
    }

    public ProductDto getProductById(String id) {
        return productMapper.productToProductDto(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found")));
    }

    public ProductDto addProduct(ProductDto productDto) {
        return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
    }

    public ProductDto updateProduct(String id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setSku(productDto.getSku());
        kafkaTemplate.send(topic, productDto.toString());

        return productMapper.productToProductDto(productRepository.save(existingProduct));
    }

    public void deleteProduct(ProductDto productDto) {
        productRepository.delete(productMapper.productDtoToProduct(productDto));
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

}
