package com.dev.ecommerce.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Long productId;
    private Integer quantity;
    private Double price;
}
