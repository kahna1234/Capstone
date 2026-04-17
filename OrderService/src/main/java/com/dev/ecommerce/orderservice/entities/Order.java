package com.dev.ecommerce.orderservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "orders")  // Table name (avoid 'order' as it's a reserved keyword)
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String customerEmail;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")  // Foreign key in order_item table
    private List<OrderItem> items;

    private String status;  // e.g., "PENDING", "CONFIRMED", "SHIPPED"
    private Double totalAmount;
}
