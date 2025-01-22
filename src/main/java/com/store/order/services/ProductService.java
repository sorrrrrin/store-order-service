package com.store.order.services;

import com.store.order.dtos.ProductDTO;
import com.store.order.entities.Product;
import com.store.order.mappers.ProductMapper;
import com.store.order.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Value("${spring.kafka.topic}")
    private String topic;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::productToProductDto).collect(Collectors.toList());
    }

    public ProductDTO getProductById(String id) {
        return productMapper.productToProductDto(productRepository.findById(id).orElse(null));
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDTO)));
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id).orElse(null);

        if (existingProduct != null) {
            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setSku(productDTO.getSku());
            kafkaTemplate.send(topic, productDTO.toString());
            return productMapper.productToProductDto(productRepository.save(existingProduct));
        }
        return null;
    }

    public void deleteProduct(ProductDTO productDTO) {
        productRepository.delete(productMapper.productDtoToProduct(productDTO));
    }


}
