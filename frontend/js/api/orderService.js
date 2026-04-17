// ============================================
// ORDER SERVICE - ES6 Module
// ============================================

import { API_CONFIG } from '../config.js';
import { StorageService } from '../utils/storage.js';

export class OrderService {
    static async createOrder(orderData) {
        try {
            const response = await fetch(`${API_CONFIG.ORDER_SERVICE}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(orderData),
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to create order');
            }

            const order = await response.json();
            return { success: true, order };

        } catch (error) {
            console.error('Error creating order:', error);
            return { success: false, error: error.message };
        }
    }

    static async getOrderById(orderId) {
        try {
            const response = await fetch(`${API_CONFIG.ORDER_SERVICE}/${orderId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Order not found');
            }

            const order = await response.json();
            return { success: true, order };

        } catch (error) {
            console.error('Error fetching order:', error);
            return { success: false, error: error.message };
        }
    }

    static async getUserOrders() {
        try {
            const user = StorageService.getUser();

            const response = await fetch(`${API_CONFIG.ORDER_SERVICE}/users/${user.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to fetch orders');
            }

            const orders = await response.json();
            return { success: true, orders };

        } catch (error) {
            console.error('Error fetching orders:', error);
            return { success: false, error: error.message };
        }
    }

    static prepareOrderData(cart, userDetails) {
        const user = StorageService.getUser();

        return {
            userId: user.id,
            items: cart.map(item => ({
                productId: item.id,
                quantity: item.quantity,
                price: item.price
            })),
            status: 'PENDING',
            totalAmount: this.calculateTotal(cart),
            customerDetails: userDetails
        };
    }

    static calculateTotal(cart) {
        return cart.reduce((total, item) => total + (item.price * item.quantity), 0);
    }
}
