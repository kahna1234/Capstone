package com.dev.ecommerce.orderservice.controllers;

import com.dev.ecommerce.orderservice.dtos.OrderDto;
import com.dev.ecommerce.orderservice.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto, HttpServletRequest request) {
        String userId = request.getHeader("X-User-Id");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        orderDto.setUserId(Long.parseLong(userId));
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id, HttpServletRequest request) {
        String userId = request.getHeader("X-User-Id");
        String userRoles = request.getHeader("X-User-Roles");
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        OrderDto order = orderService.getOrder(id);
        
        // Users can only view their own orders, admins can view all
        if (!order.getUserId().equals(Long.parseLong(userId)) && 
            (userRoles == null || !userRoles.contains("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        
        return ResponseEntity.ok(order);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId, HttpServletRequest request) {
        String requestUserId = request.getHeader("X-User-Id");
        String userRoles = request.getHeader("X-User-Roles");
        
        if (requestUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Users can only view their own orders, admins can view all
        if (!userId.equals(Long.parseLong(requestUserId)) && 
            (userRoles == null || !userRoles.contains("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpServletRequest request) {

        String userRoles = request.getHeader("X-User-Roles");
        
        if (userRoles == null || !userRoles.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }

        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    // Internal endpoint for webhook callbacks (no authentication required)
    @PutMapping("/{id}/status/webhook")
    public ResponseEntity<?> updateOrderStatusWebhook(
            @PathVariable Long id,
            @RequestParam String status) {
        
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}

