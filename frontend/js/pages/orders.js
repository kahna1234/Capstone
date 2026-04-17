// ============================================
// ORDERS PAGE - ES6 Module
// ============================================

import { OrderService } from '../api/orderService.js';
import { AuthService } from '../api/authService.js';
import { Modal } from '../components/modal.js';
import { Helpers } from '../utils/helpers.js';
import { ROUTES } from '../config.js';

export class OrdersPage {
    static async render() {
        const container = document.getElementById('app-container');
        Helpers.showLoading(container);

        if (!AuthService.isLoggedIn()) {
            container.innerHTML = `
                <div class="auth-required">
                    <h2>Please Login to View Orders</h2>
                    <a href="${ROUTES.LOGIN}" class="btn btn-primary">Login</a>
                </div>
            `;
            return;
        }

        const result = await OrderService.getUserOrders();

        if (!result.success) {
            container.innerHTML = `<div class="error">${result.error}</div>`;
            return;
        }

        const orders = result.orders || [];

        if (orders.length === 0) {
            container.innerHTML = `
                <div class="no-orders">
                    <h2>No Orders Yet</h2>
                    <p>Start shopping to place your first order</p>
                    <a href="${ROUTES.PRODUCTS}" class="btn btn-primary">Shop Now</a>
                </div>
            `;
            return;
        }

        container.innerHTML = `
            <div class="orders-container">
                <h2>My Orders</h2>
                <div class="orders-list" id="orders-list">
                    <!-- Orders will be rendered here -->
                </div>
            </div>
        `;

        const ordersList = document.getElementById('orders-list');
        orders.forEach(order => {
            ordersList.appendChild(this.renderOrderCard(order));
        });
    }

    static renderOrderCard(order) {
        const card = document.createElement('div');
        card.className = 'order-card';

        const orderTotal = order.items ? order.items.reduce((total, item) => total + (item.price * item.quantity), 0) : order.totalAmount;

        card.innerHTML = `
            <div class="order-header">
                <div>
                    <h4>Order #${order.id}</h4>
                    <p>${Helpers.formatDate(order.createdAt || new Date())}</p>
                </div>
                <div class="order-status">
                    <span class="status-badge status-${order.status.toLowerCase()}">${order.status}</span>
                </div>
            </div>
            <div class="order-items">
                ${order.items ? order.items.map(item => `
                    <div class="order-item">
                        <span>${item.quantity}x Product #${item.productId}</span>
                        <span>${Helpers.formatCurrency(item.price * item.quantity)}</span>
                    </div>
                `).join('') : '<p>No items</p>'}
            </div>
            <div class="order-footer">
                <span class="order-total">Total: ${Helpers.formatCurrency(orderTotal)}</span>
                <button class="btn btn-secondary view-details" data-order-id="${order.id}">View Details</button>
            </div>
        `;

        card.querySelector('.view-details').addEventListener('click', () => {
            this.showOrderDetails(order);
        });

        return card;
    }

    static showOrderDetails(order) {
        const content = `
            <div class="order-details">
                <p><strong>Order ID:</strong> ${order.id}</p>
                <p><strong>Status:</strong> ${order.status}</p>
                <p><strong>Total Amount:</strong> ${Helpers.formatCurrency(order.totalAmount)}</p>
                <h4>Items:</h4>
                <ul>
                    ${order.items ? order.items.map(item => `
                        <li>Product #${item.productId} - Qty: ${item.quantity} - ${Helpers.formatCurrency(item.price)}</li>
                    `).join('') : '<li>No items</li>'}
                </ul>
            </div>
        `;

        Modal.alert('Order Details', content);
    }
}
