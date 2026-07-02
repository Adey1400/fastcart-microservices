package com.fastcart.order_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fastcart.order_service.entity.Order;
import com.fastcart.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order placeOrder(Order orderRequest){
        orderRequest.setOrderNumber(UUID.randomUUID().toString());
        return orderRepository.save(orderRequest);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
