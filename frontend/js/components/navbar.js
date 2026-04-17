// ============================================
// NAVBAR COMPONENT - ES6 Module
// ============================================

import { ROUTES, APP_CONFIG } from '../config.js';
import { AuthService } from '../api/authService.js';
import { StorageService } from '../utils/storage.js';
import { Helpers } from '../utils/helpers.js';

export class Navbar {
    static render() {
        const navbar = document.getElementById('navbar');
        const isLoggedIn = AuthService.isLoggedIn();
        const user = AuthService.getCurrentUser();

        navbar.innerHTML = `
            <div class="navbar-container">
                <div class="navbar-brand">
                    <h1>${APP_CONFIG.APP_NAME}</h1>
                </div>
                <div class="navbar-search">
                    <input type="text" id="search-input" placeholder="Search products..." class="search-input">
                    <button id="search-btn" class="btn btn-search">Search</button>
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
                    window.location.hash = `${ROUTES.PRODUCTS}?search=${query}`;
                }
            });
        }

        if (searchInput) {
            searchInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    searchBtn.click();
                }
            });
        }
    }

    static updateCartCount() {
        const cartCount = document.querySelector('.cart-count');
        if (cartCount) {
            cartCount.textContent = StorageService.getCart().length;
        }
    }
}
