package com.fastcart.order_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fastcart.order_service.dto.ProductResponse;
import com.fastcart.order_service.entity.Order;
import com.fastcart.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public Order placeOrder(Order orderRequest){
        
        //Synchronous call of Product Service using web client
        ProductResponse product = webClient.get()
        .uri("http://localhost:8081/api/v1/products/" + orderRequest.getProductId())
        .retrieve()
        .bodyToMono(ProductResponse.class)
        .block(); // it blocks thread production until HTTP response return


        //Validation Check
        if (product == null){
            throw new RuntimeException("Product catalog lookup failed for ID: " + orderRequest.getProductId());
        }
        if(product.getStockQuantity()<orderRequest.getQuantity()){
            throw new RuntimeException("Inventory Shortage. Available stock: " + product.getStockQuantity());
        }

        // Securely calculate total price using DB truth, ignoring client input
        BigDecimal verifiedTotal = product.getPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity()));
        orderRequest.setTotalPrice(verifiedTotal);
         

        //Populate unique identifier details
        orderRequest.setOrderNumber(UUID.randomUUID().toString());
        return orderRepository.save(orderRequest);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
