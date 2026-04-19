package com.dev.ecommerce.cartservice.controllers;

import com.dev.ecommerce.cartservice.dtos.CartDto;
import com.dev.ecommerce.cartservice.dtos.CartItemDto;
import com.dev.ecommerce.cartservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /** GET /cart/{userId} — get or create the user's cart */
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    /** POST /cart/{userId}/items — add an item to the cart */
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDto> addItem(@PathVariable Long userId, @RequestBody CartItemDto itemDto) {
        return ResponseEntity.ok(cartService.addItem(userId, itemDto));
    }

    /** PUT /cart/{userId}/items/{itemId} — update quantity; body: { "quantity": 3 } */
    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartDto> updateItem(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @RequestBody Map<String, Integer> body) {
        Integer qty = body.get("quantity");
        if (qty == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(cartService.updateItem(userId, itemId, qty));
    }

    /** DELETE /cart/{userId}/items/{itemId} — remove one item */
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartDto> removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(userId, itemId));
    }

    /** DELETE /cart/{userId} — clear the entire cart (called after order placed) */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
