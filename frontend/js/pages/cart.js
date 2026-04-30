// ============================================
// CART PAGE - ES6 Module
// ============================================

import { StorageService } from '../utils/storage.js';
import { CartApiService } from '../api/cartService.js';
import { CartItem } from '../components/cartItem.js';
import { OrderService } from '../api/orderService.js';
import { Helpers } from '../utils/helpers.js';
import { AuthService } from '../api/authService.js';
import { Navbar } from '../components/navbar.js';
import { ROUTES } from '../config.js';

export class CartPage {
    static async render() {
        const container = document.getElementById('app-container');
        Helpers.showLoading(container);

        let cart = [];

        // If user is logged in, fetch server-side cart and sync to localStorage
        const user = StorageService.getUser();
        if (user && user.id) {
            const result = await CartApiService.getCart(user.id);
            if (result.success && result.cart && result.cart.items) {
                // Map server cart items to frontend format
                cart = result.cart.items.map(item => ({
                    id: item.productId,
                    cartItemId: item.id,      // server-side item id (for update/delete)
                    title: item.productName,
                    price: item.price,
                    image: item.imageUrl || `js/image/iphone${(item.productId % 2 === 0) ? '14' : '13'}.jpg`,
                    quantity: item.quantity
                }));
                // Keep localStorage in sync for checkout flow
                StorageService.setCart(cart);
                // Update navbar cart count
                Navbar.updateCartCount();
            } else {
                // Fall back to localStorage
                cart = StorageService.getCart();
            }
        } else {
            // Anonymous user: use localStorage only
            cart = StorageService.getCart();
        }

        if (cart.length === 0) {
            container.innerHTML = `
                <div class="empty-cart">
                    <h2>Your Cart is Empty</h2>
                    <p>Add some products to your cart</p>
                    <a href="${ROUTES.PRODUCTS}" class="btn btn-primary">Continue Shopping</a>
                </div>
            `;
            return;
        }

        const total = OrderService.calculateTotal(cart);
        const tax = total * 0.18;
        const finalTotal = total + tax;

        container.innerHTML = `
            <div class="cart-container">
                <h2>Shopping Cart</h2>
                <div class="cart-items-list" id="cart-items">
                    <!-- Items will be rendered here -->
                </div>
                <div class="cart-summary">
                    <div class="summary-row">
                        <span>Subtotal:</span>
                        <span>${Helpers.formatCurrency(total)}</span>
                    </div>
                    <div class="summary-row">
                        <span>Tax (18%):</span>
                        <span>${Helpers.formatCurrency(tax)}</span>
                    </div>
                    <div class="summary-row total">
                        <span>Total:</span>
                        <span>${Helpers.formatCurrency(finalTotal)}</span>
                    </div>
                    <button id="checkout-btn" class="btn btn-success btn-large">Proceed to Checkout</button>
                    <a href="${ROUTES.PRODUCTS}" class="btn btn-secondary">Continue Shopping</a>
                </div>
            </div>
        `;

        const cartItemsContainer = document.getElementById('cart-items');
        cart.forEach(item => {
            cartItemsContainer.appendChild(CartItem.render(item));
        });

        document.getElementById('checkout-btn').addEventListener('click', () => {
            if (!AuthService.isLoggedIn()) {
                Helpers.showError('Please login to checkout');
                window.location.hash = ROUTES.LOGIN;
                return;
            }
            window.location.hash = ROUTES.CHECKOUT;
        });
    }
}
