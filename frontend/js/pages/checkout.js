// ============================================
// CHECKOUT PAGE - ES6 Module
// ============================================

import { StorageService } from '../utils/storage.js';
import { CartApiService } from '../api/cartService.js';
import { OrderService } from '../api/orderService.js';
import { PaymentService } from '../api/paymentService.js';
import { Validators } from '../utils/validators.js';
import { Helpers } from '../utils/helpers.js';
import { Navbar } from '../components/navbar.js';
import { ROUTES } from '../config.js';

export class CheckoutPage {
    static render() {
        const container = document.getElementById('app-container');
        const cart = StorageService.getCart();

        if (cart.length === 0) {
            App.navigate(ROUTES.CART);
            Helpers.showError('Cart is empty');
            return;
        }

        const total = OrderService.calculateTotal(cart);
        const tax = total * 0.18;
        const totalWithTax = total + tax;

        container.innerHTML = `
            <div class="checkout-container">
                <h2>Checkout</h2>
                <div class="checkout-content">
                    <div class="checkout-form">
                        <h3>Delivery Address</h3>
                        <form id="checkout-form">
                            <div class="form-row">
                                <div class="form-group">
                                    <label for="first-name">First Name:</label>
                                    <input type="text" id="first-name" required>
                                </div>
                                <div class="form-group">
                                    <label for="last-name">Last Name:</label>
                                    <input type="text" id="last-name" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email">Email:</label>
                                <input type="email" id="email" required>
                            </div>
                            <div class="form-group">
                                <label for="phone">Phone Number:</label>
                                <input type="tel" id="phone" required>
                            </div>
                            <div class="form-group">
                                <label for="address">Address:</label>
                                <textarea id="address" required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Place Order</button>
                        </form>
                    </div>

                    <div class="checkout-summary">
                        <h3>Order Summary</h3>
                        <div id="order-items">
                            <!-- Items will be rendered here -->
                        </div>
                        <div class="summary-row">
                            <span>Subtotal:</span>
                            <span>${Helpers.formatCurrency(total)}</span>
                        </div>
                        <div class="summary-row">
                            <span>Tax (18%):</span>
                            <span>${Helpers.formatCurrency(tax)}</span>
                        </div>
                        <div class="summary-row total">
                            <span>Total Amount:</span>
                            <span>${Helpers.formatCurrency(totalWithTax)}</span>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // Render order items
        const orderItemsContainer = document.getElementById('order-items');
        cart.forEach(item => {
            const itemElement = document.createElement('div');
            itemElement.className = 'summary-item';
            itemElement.innerHTML = `
                <span>${item.title} x ${item.quantity}</span>
                <span>${Helpers.formatCurrency(item.price * item.quantity)}</span>
            `;
            orderItemsContainer.appendChild(itemElement);
        });

        document.getElementById('checkout-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleCheckout();
        });
    }

    static async handleCheckout() {
        const submitButton = document.querySelector('#checkout-form button[type="submit"]');
        if (submitButton?.disabled) {
            return;
        }

        const firstName = document.getElementById('first-name').value;
        const lastName = document.getElementById('last-name').value;
        const email = document.getElementById('email').value;
        const phone = document.getElementById('phone').value;
        const address = document.getElementById('address').value;

        const validation = Validators.validateCheckoutForm(firstName, lastName, email, phone, address);
        if (!validation.isValid) {
            validation.errors.forEach(error => Helpers.showError(error));
            return;
        }

        if (submitButton) {
            submitButton.disabled = true;
            submitButton.textContent = 'Processing...';
        }

        const cart = StorageService.getCart();
        const user = StorageService.getUser();

        try {
            const orderData = {
                userId: user.id,
                customerEmail: email,
                items: cart.map(item => ({
                    productId: item.id,
                    quantity: item.quantity,
                    price: item.price
                })),
                status: 'PENDING',
                totalAmount: OrderService.calculateTotal(cart) * 1.18
            };

            const result = await OrderService.createOrder(orderData);
            if (!result.success) {
                Helpers.showError(result.error || 'Failed to create order');
                if (submitButton) {
                    submitButton.disabled = false;
                    submitButton.textContent = 'Place Order';
                }
                return;
            }

            const order = result.order;
            const total = OrderService.calculateTotal(cart) * 1.18;

            // Initiate payment
            const paymentResult = await PaymentService.generatePaymentLink({
                orderId: order.id,
                amount: total,
                phoneNumber: phone,
                name: `${firstName} ${lastName}`,
                email: email,
                gateway: 'STRIPE'
            });

            if (paymentResult.success) {
                // Clear localStorage cart
                StorageService.clearCart();
                Navbar.updateCartCount();
                // Also clear server-side cart
                const user = StorageService.getUser();
                if (user && user.id) {
                    await CartApiService.clearCart(user.id);
                }
                Helpers.showSuccess('Order created! Redirecting to payment...');

                setTimeout(() => {
                    PaymentService.openPaymentGateway(paymentResult.paymentLink);
                    App.navigate(ROUTES.HOME);
                }, 2000);
            } else {
                Helpers.showError(paymentResult.error || 'Payment failed');
                if (submitButton) {
                    submitButton.disabled = false;
                    submitButton.textContent = 'Place Order';
                }
            }
        } catch (error) {
            Helpers.showError(error.message || 'Checkout failed');
            if (submitButton) {
                submitButton.disabled = false;
                submitButton.textContent = 'Place Order';
            }
        }
    }
}
