package com.dev.ecommerce.orderservice.services;

import com.dev.ecommerce.orderservice.dtos.OrderDto;

import java.util.List;

public interface OrderServiceInterface {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto updateOrderStatus(Long orderId, String status);
}
