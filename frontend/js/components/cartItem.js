// ============================================
// CART ITEM COMPONENT - ES6 Module
// ============================================

import { StorageService } from '../utils/storage.js';
import { Helpers } from '../utils/helpers.js';
import { Navbar } from './navbar.js';
import { CartPage } from '../pages/cart.js';
import { CartApiService } from '../api/cartService.js';
import { AuthService } from '../api/authService.js';

export class CartItem {
    static render(item) {
        const cartItem = document.createElement('div');
        cartItem.className = 'cart-item';

        cartItem.innerHTML = `
            <img src="${item.image}" alt="${item.title}" class="cart-item-image">
            <div class="cart-item-details">
                <h4>${item.title}</h4>
                <p>Price: ${Helpers.formatCurrency(item.price)}</p>
            </div>
            <div class="cart-item-quantity">
                <button class="qty-btn decrease" data-product-id="${item.id}">-</button>
                <input type="number" value="${item.quantity}" class="qty-input" data-product-id="${item.id}">
                <button class="qty-btn increase" data-product-id="${item.id}">+</button>
            </div>
            <div class="cart-item-subtotal">
                ${Helpers.formatCurrency(item.price * item.quantity)}
            </div>
            <button class="btn btn-danger remove-btn" data-product-id="${item.id}">Remove</button>
        `;

        const decreaseBtn = cartItem.querySelector('.decrease');
        const increaseBtn = cartItem.querySelector('.increase');
        const qtyInput = cartItem.querySelector('.qty-input');
        const removeBtn = cartItem.querySelector('.remove-btn');

        decreaseBtn.addEventListener('click', async () => {
            const newQty = Math.max(1, item.quantity - 1);
            if (AuthService.isLoggedIn()) {
                const user = StorageService.getUser();
                await CartApiService.updateItem(user.id, item.cartItemId, newQty);
            } else {
                StorageService.updateCartItem(item.id, newQty);
            }
            CartPage.render();
        });

        increaseBtn.addEventListener('click', async () => {
            if (AuthService.isLoggedIn()) {
                const user = StorageService.getUser();
                await CartApiService.updateItem(user.id, item.cartItemId, item.quantity + 1);
            } else {
                StorageService.updateCartItem(item.id, item.quantity + 1);
            }
            CartPage.render();
        });

        qtyInput.addEventListener('change', async () => {
            const newQty = Math.max(1, parseInt(qtyInput.value) || 1);
            if (AuthService.isLoggedIn()) {
                const user = StorageService.getUser();
                await CartApiService.updateItem(user.id, item.cartItemId, newQty);
            } else {
                StorageService.updateCartItem(item.id, newQty);
            }
            CartPage.render();
        });

        removeBtn.addEventListener('click', async () => {
            if (AuthService.isLoggedIn()) {
                const user = StorageService.getUser();
                await CartApiService.removeItem(user.id, item.cartItemId);
            } else {
                StorageService.removeFromCart(item.id);
            }
            Navbar.updateCartCount();
            CartPage.render();
            Helpers.showSuccess('Item removed from cart');
        });

        return cartItem;
    }
}
