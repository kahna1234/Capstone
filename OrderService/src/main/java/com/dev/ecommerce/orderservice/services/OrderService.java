package com.dev.ecommerce.orderservice.services;

import com.dev.ecommerce.orderservice.clients.KafkaProducerClient;
import com.dev.ecommerce.orderservice.clients.InventoryServiceClient;
import com.dev.ecommerce.orderservice.dtos.OrderDto;
import com.dev.ecommerce.orderservice.dtos.OrderItemDto;
import com.dev.ecommerce.orderservice.entities.Order;
import com.dev.ecommerce.orderservice.entities.OrderItem;
import com.dev.ecommerce.orderservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceInterface {

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    public OrderDto createOrder(OrderDto orderDto) {
        // Check inventory availability before creating order
        for (OrderItemDto item : orderDto.getItems()) {
            if (!inventoryServiceClient.checkInventory(item.getProductId(), item.getQuantity())) {
                throw new RuntimeException("Insufficient inventory for product ID: " + item.getProductId());
            }
        }

        // Convert DTO to Entity
        Order order = new Order();
        order.setUserId(orderDto.getUserId());
        order.setCustomerEmail(orderDto.getCustomerEmail());
        order.setStatus(orderDto.getStatus());
        order.setTotalAmount(orderDto.getTotalAmount());
        List<OrderItem> items = orderDto.getItems().stream().map(dto -> {
            OrderItem item = new OrderItem();
            item.setProductId(dto.getProductId());
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());
            return item;
        }).collect(Collectors.toList());
        order.setItems(items);

        // Save to database (Hibernate creates/updates tables)
        Order savedOrder = orderRepository.save(order);

        // Convert back to DTO
        orderDto.setId(savedOrder.getId());

        // Note: Inventory reduction will happen when order status is updated to "CONFIRMED"
        // This prevents double deduction during order creation and confirmation

        return orderDto;
    }

    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setItems(order.getItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setProductId(item.getProductId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }

    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setUserId(order.getUserId());
            dto.setCustomerEmail(order.getCustomerEmail());
            dto.setStatus(order.getStatus());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setItems(order.getItems().stream().map(item -> {
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setProductId(item.getProductId());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPrice(item.getPrice());
                return itemDto;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public OrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            if (order.getStatus() != null && order.getStatus().equalsIgnoreCase(status)) {
                return getOrder(orderId);
            }

            order.setStatus(status);
            orderRepository.save(order);
            OrderDto updatedOrder = getOrder(orderId);
            if ("CONFIRMED".equalsIgnoreCase(status)) {
                kafkaProducerClient.sendOrderConfirmation(updatedOrder);
            }
            return updatedOrder;  // Return updated DTO
        }
        return null;
    }
}
