// ============================================
// PRODUCT CARD COMPONENT - ES6 Module
// ============================================

import { StorageService } from '../utils/storage.js';
import { Helpers } from '../utils/helpers.js';
import { Navbar } from './navbar.js';

export class ProductCard {
    static render(product) {
        const card = document.createElement('div');
        card.className = 'product-card';

        // List of available images in your js/image folder
        const availableImages = [
            'iphone13.jpg', 
            'iphone14.jpg'
        ];
        
        // Pick a "random" image but keep it consistent for the same product using its ID
        const randomImageName = availableImages[(product.id || 0) % availableImages.length];

        // Always take the image directly from the local image folder, ignoring the incorrect database URL
        const imagePath = `js/image/${randomImageName}`;

        card.innerHTML = `
            <div class="product-image">
                <img src="${imagePath}" alt="${product.title || product.name || 'Product'}">
            </div>
            <div class="product-info">
                <h3 class="product-title">${product.title || product.name || 'Product'}</h3>
                <p class="product-description">${product.description || ''}</p>
                <div class="product-footer" style="display: flex; flex-direction: column; gap: 10px; margin-top: 15px;">
                    <span class="product-price" style="font-weight: bold; font-size: 1.1em;">${Helpers.formatCurrency(product.price)}</span>
                    <button class="btn btn-primary add-to-cart-btn" data-product-id="${product.id}" style="width: 100%;">
                        Add to Cart
                    </button>
                </div>
            </div>
        `;

        const addBtn = card.querySelector('.add-to-cart-btn');
        addBtn.addEventListener('click', () => {
            StorageService.addToCart({
                id: product.id,
                title: product.title || product.name || 'Product',
                price: product.price,
                image: imagePath,
                quantity: 1
            });
            Navbar.updateCartCount();
            Helpers.showSuccess(`${product.title || product.name || 'Product'} added to cart!`);
        });

        return card;
    }
}
