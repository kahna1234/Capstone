// ============================================
// SEARCH SERVICE - ES6 Module
// ============================================

import { API_CONFIG, APP_CONFIG } from '../config.js';

export class SearchService {
    static async searchProducts(query, page = 0, size = APP_CONFIG.ITEMS_PER_PAGE, sortParams = {}) {
        try {
            console.log('[SearchService] Searching products with query:', query);

            const response = await fetch(`${API_CONFIG.PRODUCT_SERVICE}/search?q=${encodeURIComponent(query)}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Search failed');
            }

            const results = await response.json();
            console.log('[SearchService] Raw API response:', results);
            
            // Check for duplicates in the raw response
            let products = Array.isArray(results) ? results : (results.content || results.results || []);
            console.log('[SearchService] Extracted products array:', products);
            
            // Log detailed product information for debugging
            products.forEach((product, index) => {
                console.log(`[SearchService] Product ${index}:`, {
                    id: product.id,
                    title: product.title || product.name,
                    price: product.price
                });
            });
            
            return { success: true, results };

        } catch (error) {
            console.error('[SearchService] Error searching products:', error);
            return { success: false, error: error.message };
        }
    }

    static async getSearchSuggestions(query, limit = 5) {
        try {
            console.log('Getting search suggestions for:', query);

            if (query.trim().length < 2) {
                return { success: true, suggestions: [] };
            }

            const response = await fetch(`${API_CONFIG.PRODUCT_SERVICE}/search/suggestions?q=${encodeURIComponent(query)}&limit=${limit}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                // Fallback to basic search if suggestions endpoint doesn't exist
                const searchResult = await this.searchProducts(query, 0, limit);
                if (searchResult.success && searchResult.results) {
                    const suggestions = searchResult.results.content || searchResult.results || [];
                    return { 
                        success: true, 
                        suggestions: suggestions.slice(0, limit).map(product => ({
                            id: product.id,
                            title: product.title || product.name,
                            description: product.description,
                            price: product.price,
                            image: product.imageUrl || product.image_url
                        }))
                    };
                }
                throw new Error('Failed to get suggestions');
            }

            const suggestions = await response.json();
            console.log('Search suggestions:', suggestions);
            return { success: true, suggestions };

        } catch (error) {
            console.error('Error getting search suggestions:', error);
            return { success: false, error: error.message, suggestions: [] };
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
