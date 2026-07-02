package com.fastcart.order_service.entity;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UUID for external reference, much harder to guess than an auto-incrementing ID
    @Column(nullable = false, unique = true)
    private String orderNumber; 

    // Notice this is just a Long. We do NOT use @ManyToOne with the Product class here.
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;
}