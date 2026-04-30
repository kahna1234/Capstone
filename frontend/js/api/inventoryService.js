// ============================================
// INVENTORY SERVICE API - ES6 Module
// ============================================

import { API_CONFIG } from '../config.js';

export class InventoryService {

    /**
     * Get inventory for a specific product.
     * Returns { success, inventory: { productId, quantity, ... } }
     */
    static async getInventory(productId) {
        try {
            const response = await fetch(`${API_CONFIG.INVENTORY_SERVICE}/${productId}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            });

            if (!response.ok) {
                return { success: false, error: 'Inventory not found' };
            }

            const inventory = await response.json();
            return { success: true, inventory };
        } catch (error) {
            console.error('[InventoryService] getInventory error:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Get all inventory records (admin use).
     */
    static async getAllInventory() {
        try {
            const response = await fetch(`${API_CONFIG.INVENTORY_SERVICE}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            });

            if (!response.ok) throw new Error('Failed to fetch inventory');
            const inventoryList = await response.json();
            return { success: true, inventoryList };
        } catch (error) {
            console.error('[InventoryService] getAllInventory error:', error);
            return { success: false, error: error.message };
        }
    }

    /**
     * Admin: manually update stock quantity for a product.
     * @param {number} productId
     * @param {number} quantity - new total quantity to set
     */
    static async updateInventory(productId, quantity) {
        try {
            const response = await fetch(`${API_CONFIG.INVENTORY_SERVICE}/${productId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ quantity })
            });

            if (!response.ok) throw new Error('Failed to update inventory');
            const inventory = await response.json();
            return { success: true, inventory };
        } catch (error) {
            console.error('[InventoryService] updateInventory error:', error);
            return { success: false, error: error.message };
        }
    }
}
