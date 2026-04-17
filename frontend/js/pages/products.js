// ============================================
// PRODUCTS PAGE - ES6 Module
// ============================================

import { ProductService } from '../api/productService.js';
import { SearchService } from '../api/searchService.js';
import { ProductCard } from '../components/productCard.js';
import { Helpers } from '../utils/helpers.js';

export class ProductsPage {
    static async render(searchQuery = '') {
        const container = document.getElementById('app-container');
        Helpers.showLoading(container);

        let result;
        if (searchQuery) {
            result = await SearchService.searchProducts(searchQuery);
        } else {
            result = await ProductService.getAllProducts();
        }

        if (!result.success) {
            container.innerHTML = `<div class="error">${result.error}</div>`;
            return;
        }

        const products = result.products || result.results || [];

        container.innerHTML = `
            <div class="products-container">
                <div class="products-header">
                    <h2>${searchQuery ? `Search Results for "${searchQuery}"` : 'Our Products'}</h2>
                </div>
                <div class="products-grid" id="products-grid">
                    ${products.length === 0 ? '<p>No products found</p>' : ''}
                </div>
            </div>
        `;

        const productsGrid = document.getElementById('products-grid');
        
        if (Array.isArray(products)) {
            products.forEach(product => {
                productsGrid.appendChild(ProductCard.render(product));
            });
        } else if (products.content) {
            // Handle paginated response
            products.content.forEach(product => {
                productsGrid.appendChild(ProductCard.render(product));
            });
        }
    }
}
