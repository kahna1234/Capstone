// ============================================
// SEARCH SERVICE - ES6 Module
// ============================================

import { API_CONFIG, APP_CONFIG } from '../config.js';

export class SearchService {
    static async searchProducts(query, page = 0, size = APP_CONFIG.ITEMS_PER_PAGE, sortParams = {}) {
        try {
            console.log('Searching products with query:', query);

            const response = await fetch(`${API_CONFIG.SEARCH_SERVICE}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    query,
                    page,
                    size,
                    sortParams
                }),
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Search failed');
            }

            const results = await response.json();
            console.log('Search results:', results);
            return { success: true, results };

        } catch (error) {
            console.error('Error searching products:', error);
            return { success: false, error: error.message };
        }
    }

    static async searchByCategory(category, page = 0) {
        console.log('Searching by category:', category);
        return this.searchProducts(category, page);
    }

    static async searchByPrice(minPrice, maxPrice, page = 0) {
        console.log('Searching by price range:', minPrice, '-', maxPrice);
        return this.searchProducts(`price:${minPrice}-${maxPrice}`, page);
    }
}
