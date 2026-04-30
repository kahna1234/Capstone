// ============================================
// NAVBAR COMPONENT - ES6 Module
// ============================================

import { ROUTES, APP_CONFIG } from '../config.js';
import { AuthService } from '../api/authService.js';
import { StorageService } from '../utils/storage.js';
import { Helpers } from '../utils/helpers.js';
import { SearchService } from '../api/searchService.js';

export class Navbar {
    static render() {
        const navbar = document.getElementById('navbar');
        const isLoggedIn = AuthService.isLoggedIn();
        const user = AuthService.getCurrentUser();
        console.log('[Navbar] render - isLoggedIn:', isLoggedIn, 'user:', user);

        navbar.innerHTML = `
            <div class="navbar-container">
                <div class="navbar-brand">
                    <h1>${APP_CONFIG.APP_NAME}</h1>
                </div>
                <div class="navbar-search">
                    <div class="search-container">
                        <input type="text" id="search-input" placeholder="Search products..." class="search-input">
                        <button id="search-btn" class="btn btn-search">Search</button>
                        <div id="search-suggestions" class="search-suggestions"></div>
                    </div>
                </div>
                <div class="navbar-menu">
                    <a href="${ROUTES.HOME}" class="nav-link">Home</a>
                    <a href="${ROUTES.PRODUCTS}" class="nav-link">Products</a>
                    
                    ${isLoggedIn ? `
                        <a href="${ROUTES.ORDERS}" class="nav-link">My Orders</a>
                        <span class="nav-text">Welcome, ${user.name}</span>
                        <button id="logout-btn" class="btn btn-danger">Logout</button>
                    ` : `
                        <a href="${ROUTES.LOGIN}" class="btn btn-primary">Login</a>
                        <a href="${ROUTES.SIGNUP}" class="btn btn-secondary">Signup</a>
                    `}

                    <a href="${ROUTES.CART}" class="nav-link">
                        Cart <span class="cart-count">${StorageService.getCart().length}</span>
                    </a>
                </div>
            </div>
        `;

        this.attachEventListeners();
    }

    static attachEventListeners() {
        const logoutBtn = document.getElementById('logout-btn');
        const searchBtn = document.getElementById('search-btn');
        const searchInput = document.getElementById('search-input');
        const searchSuggestions = document.getElementById('search-suggestions');

        if (logoutBtn) {
            logoutBtn.addEventListener('click', async () => {
                await AuthService.logout();
                window.location.hash = ROUTES.HOME;
                Helpers.showSuccess('Logged out successfully');
                setTimeout(() => {
                    window.location.reload();
                }, 1000);
            });
        }

        if (searchBtn) {
            searchBtn.addEventListener('click', () => {
                const query = searchInput.value.trim();
                if (query) {
                    this.hideSuggestions();
                    window.location.hash = `${ROUTES.PRODUCTS}?search=${encodeURIComponent(query)}`;
                }
            });
        }

        if (searchInput) {
            let debounceTimer;
            
            // Input event for autosuggestions
            searchInput.addEventListener('input', async (e) => {
                const query = e.target.value.trim();
                
                // Clear previous timer
                clearTimeout(debounceTimer);
                
                if (query.length < 2) {
                    this.hideSuggestions();
                    return;
                }
                
                // Debounce API call
                debounceTimer = setTimeout(async () => {
                    await this.fetchSuggestions(query);
                }, 300);
            });

            
            
            // Hide suggestions when clicking outside
            document.addEventListener('click', (e) => {
                if (!e.target.closest('.search-container')) {
                    this.hideSuggestions();
                }
            });
        }
    }

    static async fetchSuggestions(query) {
        const searchSuggestions = document.getElementById('search-suggestions');
        
        try {
            const result = await SearchService.getSearchSuggestions(query, 5);
            
            if (result.success && result.suggestions && result.suggestions.length > 0) {
                this.showSuggestions(result.suggestions);
            } else {
                this.hideSuggestions();
            }
        } catch (error) {
            console.error('[Navbar] Error fetching suggestions:', error);
            this.hideSuggestions();
        }
    }

    static showSuggestions(suggestions) {
        const searchSuggestions = document.getElementById('search-suggestions');
        const searchInput = document.getElementById('search-input');
        
        const suggestionsHTML = suggestions.map((suggestion, index) => `
            <div class="suggestion-item" data-index="${index}" data-query="${suggestion.title || suggestion.name}">
                <div class="suggestion-content">
                    ${suggestion.image ? `<img src="${suggestion.image}" alt="${suggestion.title || suggestion.name}" class="suggestion-image">` : ''}
                    <div class="suggestion-details">
                        <div class="suggestion-title">${suggestion.title || suggestion.name}</div>
                        ${suggestion.description ? `<div class="suggestion-description">${suggestion.description}</div>` : ''}
                        ${suggestion.price ? `<div class="suggestion-price">${Helpers.formatCurrency(suggestion.price)}</div>` : ''}
                    </div>
                </div>
            </div>
        `).join('');

        searchSuggestions.innerHTML = suggestionsHTML;
        searchSuggestions.classList.add('show');
        
        // Add click listeners to suggestions
        searchSuggestions.querySelectorAll('.suggestion-item').forEach(item => {
            item.addEventListener('click', () => {
                const query = item.dataset.query;
                searchInput.value = query;
                this.hideSuggestions();
                
                // Direct navigation instead of triggering button click to avoid duplicates
                if (query.trim()) {
                    window.location.hash = `${ROUTES.PRODUCTS}?search=${encodeURIComponent(query)}`;
                }
            });
        });
    }

    static hideSuggestions() {
        const searchSuggestions = document.getElementById('search-suggestions');
        searchSuggestions.innerHTML = '';
        searchSuggestions.classList.remove('show');
    }

    static handleKeyboardNavigation(e) {
        const searchSuggestions = document.getElementById('search-suggestions');
        const suggestionItems = searchSuggestions.querySelectorAll('.suggestion-item');
        
        if (!searchSuggestions.classList.contains('show') || suggestionItems.length === 0) {
            return;
        }

        const activeItem = searchSuggestions.querySelector('.suggestion-item.active');
        let currentIndex = activeItem ? parseInt(activeItem.dataset.index) : -1;

        switch (e.key) {
            case 'ArrowDown':
                e.preventDefault();
                currentIndex = (currentIndex + 1) % suggestionItems.length;
                this.highlightSuggestion(currentIndex);
                break;
            case 'ArrowUp':
                e.preventDefault();
                currentIndex = currentIndex <= 0 ? suggestionItems.length - 1 : currentIndex - 1;
                this.highlightSuggestion(currentIndex);
                break;
            case 'Enter':
                e.preventDefault();
                if (activeItem) {
                    const query = activeItem.dataset.query;
                    searchInput.value = query;
                    this.hideSuggestions();
                    
                    // Direct navigation instead of triggering click to avoid duplicates
                    if (query.trim()) {
                        window.location.hash = `${ROUTES.PRODUCTS}?search=${encodeURIComponent(query)}`;
                    }
                }
                break;
            case 'Escape':
                this.hideSuggestions();
                break;
        }
    }

    static highlightSuggestion(index) {
        const searchSuggestions = document.getElementById('search-suggestions');
        const suggestionItems = searchSuggestions.querySelectorAll('.suggestion-item');
        
        // Remove active class from all items
        suggestionItems.forEach(item => item.classList.remove('active'));
        
        // Add active class to selected item
        if (index >= 0 && index < suggestionItems.length) {
            suggestionItems[index].classList.add('active');
            
            // Update input value to match selected suggestion
            const selectedItem = suggestionItems[index];
            const searchInput = document.getElementById('search-input');
            searchInput.value = selectedItem.dataset.query;
        }
    }

    static updateCartCount() {
        const cartCount = document.querySelector('.cart-count');
        if (cartCount) {
            const cart = StorageService.getCart();
            const totalItems = cart.reduce((sum, item) => sum + (item.quantity || 1), 0);
            cartCount.textContent = totalItems;
        }
    }

    static async syncCartFromServer() {
        const user = StorageService.getUser();
        if (user && user.id) {
            try {
                const { CartApiService } = await import('../api/cartService.js');
                const result = await CartApiService.getCart(user.id);
                if (result.success && result.cart && result.cart.items) {
                    // Map server cart items to frontend format
                    const cart = result.cart.items.map(item => ({
                        id: item.productId,
                        cartItemId: item.id,
                        title: item.productName,
                        price: item.price,
                        image: item.imageUrl || `js/image/iphone${(item.productId % 2 === 0) ? '14' : '13'}.jpg`,
                        quantity: item.quantity
                    }));
                    // Update local storage
                    StorageService.setCart(cart);
                    // Update cart count display
                    this.updateCartCount();
                }
            } catch (error) {
                console.error('[Navbar] Failed to sync cart from server:', error);
            }
        }
    }
}
