package com.dev.ecommerce.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long userId;
    private String customerEmail;
    private List<OrderItemDto> items;
    private String status;  // e.g., "PENDING", "CONFIRMED", "SHIPPED"
    private Double totalAmount;
}
