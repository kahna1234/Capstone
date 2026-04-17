// ============================================
// PRODUCT SERVICE - ES6 Module
// ============================================

import { API_CONFIG, APP_CONFIG } from '../config.js';
import { StorageService } from '../utils/storage.js';

export class ProductService {
    static async getAllProducts(page = 0, size = APP_CONFIG.ITEMS_PER_PAGE) {
        try {
            console.log('Fetching products from:', `${API_CONFIG.PRODUCT_SERVICE}`);

            const response = await fetch(`${API_CONFIG.PRODUCT_SERVICE}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to fetch products');
            }

            const products = await response.json();
            console.log('Products fetched successfully:', products);
            return { success: true, products };

        } catch (error) {
            console.error('Error fetching products:', error);
            return { success: false, error: error.message };
        }
    }

    static async getProductById(id) {
        try {
            console.log('Fetching product by ID:', id);

            const response = await fetch(`${API_CONFIG.PRODUCT_SERVICE}/${id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Product not found');
            }

            const product = await response.json();
            console.log('Product fetched:', product);
            return { success: true, product };

        } catch (error) {
            console.error('Error fetching product:', error);
            return { success: false, error: error.message };
        }
    }

    static async createProduct(productData) {
        try {
            const token = StorageService.getToken();
            console.log('Creating product');

            const response = await fetch(`${API_CONFIG.PRODUCT_SERVICE}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(productData),
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to create product');
            }

            const product = await response.json();
            console.log('Product created:', product);
            return { success: true, product };

        } catch (error) {
            console.error('Error creating product:', error);
            return { success: false, error: error.message };
        }
    }

    static async updateProduct(id, productData) {
        try {
            const token = StorageService.getToken();
            console.log('Updating product:', id);

            const response = await fetch(`${API_CONFIG.PRODUCT_SERVICE}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(productData),
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to update product');
            }

            const product = await response.json();
            console.log('Product updated:', product);
            return { success: true, product };

        } catch (error) {
            console.error('Error updating product:', error);
            return { success: false, error: error.message };
        }
    }

    static async deleteProduct(id) {
        try {
            const token = StorageService.getToken();
            console.log('Deleting product:', id);

            const response = await fetch(`${API_CONFIG.PRODUCT_SERVICE}/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to delete product');
            }

            console.log('Product deleted successfully');
            return { success: true };

        } catch (error) {
            console.error('Error deleting product:', error);
            return { success: false, error: error.message };
        }
    }
}
