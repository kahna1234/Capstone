// ============================================
// CART SERVICE API - ES6 Module
// ============================================

import { API_CONFIG } from '../config.js';

export class CartApiService {

    /**
     * Get the user's cart from the server.
     * Returns { success, cart: { userId, items: [...], total } }
     */
    static async getCart(userId) {
        try {
            const response = await fetch(`${API_CONFIG.CART_SERVICE}/${userId}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            });

            if (!response.ok) throw new Error('Failed to fetch cart');
            const cart = await response.json();
            return { success: true, cart };
        } catch (error) {
            console.error('[CartApiService] getCart error:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Add an item to the server-side cart.
     * @param {number} userId
     * @param {{ productId, productName, price, quantity, imageUrl }} item
     */
    static async addToCart(userId, item) {
        try {
            const response = await fetch(`${API_CONFIG.CART_SERVICE}/${userId}/items`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(item)
            });

            if (!response.ok) throw new Error('Failed to add item to cart');
            const cart = await response.json();
            return { success: true, cart };
        } catch (error) {
            console.error('[CartApiService] addToCart error:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Update the quantity of a cart item.
     * @param {number} userId
     * @param {number} itemId - the CartItem id (from server)
     * @param {number} quantity - new quantity
     */
    static async updateItem(userId, itemId, quantity) {
        try {
            const response = await fetch(`${API_CONFIG.CART_SERVICE}/${userId}/items/${itemId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ quantity })
            });

            if (!response.ok) throw new Error('Failed to update cart item');
            const cart = await response.json();
            return { success: true, cart };
        } catch (error) {
            console.error('[CartApiService] updateItem error:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Remove a single item from the server-side cart.
     */
    static async removeItem(userId, itemId) {
        try {
            const response = await fetch(`${API_CONFIG.CART_SERVICE}/${userId}/items/${itemId}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (!response.ok) throw new Error('Failed to remove cart item');
            const cart = await response.json();
            return { success: true, cart };
        } catch (error) {
            console.error('[CartApiService] removeItem error:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Clear the entire cart (call after order is placed).
     */
    static async clearCart(userId) {
        try {
            const response = await fetch(`${API_CONFIG.CART_SERVICE}/${userId}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            return { success: response.ok };
        } catch (error) {
            console.error('[CartApiService] clearCart error:', error);
            return { success: false, error: error.message };
        }
    }
}
