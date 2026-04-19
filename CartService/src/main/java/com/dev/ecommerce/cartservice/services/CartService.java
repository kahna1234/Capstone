package com.dev.ecommerce.cartservice.services;

import com.dev.ecommerce.cartservice.dtos.CartDto;
import com.dev.ecommerce.cartservice.dtos.CartItemDto;
import com.dev.ecommerce.cartservice.entities.Cart;
import com.dev.ecommerce.cartservice.entities.CartItem;
import com.dev.ecommerce.cartservice.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    /** Get or create cart for a user */
    public CartDto getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return toDto(cart);
    }

    /** Add an item to the cart; increments quantity if product already in cart */
    public CartDto addItem(Long userId, CartItemDto itemDto) {
        Cart cart = getOrCreateCart(userId);

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(itemDto.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + (itemDto.getQuantity() != null ? itemDto.getQuantity() : 1));
        } else {
            CartItem item = new CartItem();
            item.setProductId(itemDto.getProductId());
            item.setProductName(itemDto.getProductName());
            item.setPrice(itemDto.getPrice());
            item.setQuantity(itemDto.getQuantity() != null ? itemDto.getQuantity() : 1);
            item.setImageUrl(itemDto.getImageUrl());
            cart.getItems().add(item);
        }

        cart.setLastUpdatedAt(new Date());
        cartRepository.save(cart);
        return toDto(cart);
    }

    /** Update quantity of a specific cart item */
    public CartDto updateItem(Long userId, Long itemId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);

        cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .ifPresent(i -> {
                    if (quantity <= 0) {
                        cart.getItems().remove(i);
                    } else {
                        i.setQuantity(quantity);
                    }
                });

        cart.setLastUpdatedAt(new Date());
        cartRepository.save(cart);
        return toDto(cart);
    }

    /** Remove a single item from the cart */
    public CartDto removeItem(Long userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        cart.setLastUpdatedAt(new Date());
        cartRepository.save(cart);
        return toDto(cart);
    }

    /** Clear all items (called after order is placed) */
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getItems().clear();
            cart.setLastUpdatedAt(new Date());
            cartRepository.save(cart);
        });
    }

    // ----------------------------------------------------------------
    // Helper methods
    // ----------------------------------------------------------------

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setCreatedAt(new Date());
            newCart.setLastUpdatedAt(new Date());
            return cartRepository.save(newCart);
        });
    }

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());

        List<CartItemDto> items = cart.getItems().stream().map(i -> {
            CartItemDto iDto = new CartItemDto();
            iDto.setId(i.getId());
            iDto.setProductId(i.getProductId());
            iDto.setProductName(i.getProductName());
            iDto.setPrice(i.getPrice());
            iDto.setQuantity(i.getQuantity());
            iDto.setImageUrl(i.getImageUrl());
            return iDto;
        }).collect(Collectors.toList());

        dto.setItems(items);
        dto.setTotal(items.stream()
                .mapToDouble(i -> (i.getPrice() != null ? i.getPrice() : 0.0) * (i.getQuantity() != null ? i.getQuantity() : 0))
                .sum());
        return dto;
    }
}
