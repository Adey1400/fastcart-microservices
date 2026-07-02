package com.fastcart.product_service.service;



import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fastcart.product_service.entity.Product;
import com.fastcart.product_service.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    
    private final ProductRepository productRepository;
    
    // @CacheEvict clears the "products" cache when a new item is added
    // so our cache doesn't serve outdated catalog lists.
    @CacheEvict(value="products", allEntries = true)
    public Product createProduct(Product product) {
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if(productRepository.existsByName(product.getName())){
    throw new RuntimeException("Product already exists");
}
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        // In a production app, we would throw a custom exception here. We'll add that in Milestone 11.
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
}