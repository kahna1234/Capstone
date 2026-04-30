// ============================================
// MAIN APPLICATION ROUTER - ES6 Module
// ============================================

import { ROUTES, APP_CONFIG } from './config.js';
import { AuthService } from './api/authService.js';
import { Navbar } from './components/navbar.js';
import { AuthPage } from './pages/auth.js';
import { ProductsPage } from './pages/products.js';
import { CartPage } from './pages/cart.js';
import { CheckoutPage } from './pages/checkout.js';
import { OrdersPage } from './pages/orders.js';
import { PaymentSuccessPage } from './pages/paymentSuccess.js';

class App {
    static currentRoute = ROUTES.HOME;
    static currentParams = {};

    static init() {
        Navbar.render();
        this.setupRouting();
        this.renderCurrentHash();
    }

    static setupRouting() {
        window.addEventListener('hashchange', () => {
            this.renderCurrentHash();
        });
    }

    static renderCurrentHash() {
        const hash = window.location.hash || ROUTES.HOME;
        const [route, queryString = ''] = hash.split('?');
        const queryParams = Object.fromEntries(new URLSearchParams(queryString));

        this.navigate(route, queryParams);
    }

    static navigate(route, params = {}) {
        this.currentRoute = route;
        this.currentParams = params;
        const currentRoute = (window.location.hash || '').split('?')[0];
        if (currentRoute !== route) {
            window.location.hash = route;
        }

        // Re-render navbar on every route change to update login state
        Navbar.render();

        switch (route) {
            case ROUTES.HOME:
                this.renderHome();
                break;
            case ROUTES.PRODUCTS:
                ProductsPage.render(params.search || '');
                break;
            case ROUTES.CART:
                CartPage.render();
                break;
            case ROUTES.CHECKOUT:
                CheckoutPage.render();
                break;
            case ROUTES.ORDERS:
                OrdersPage.render();
                break;
            case ROUTES.PAYMENT_SUCCESS:
                PaymentSuccessPage.render(params.orderId || '');
                break;
            case ROUTES.LOGIN:
                AuthPage.renderLogin();
                break;
            case ROUTES.SIGNUP:
                AuthPage.renderSignup();
                break;
            default:
                this.renderHome();
        }
    }

    static renderHome() {
        const container = document.getElementById('app-container');
        const isLoggedIn = AuthService.isLoggedIn();

        container.innerHTML = `
            <div class="home-container">
                <section class="hero">
                    <div class="hero-content">
                        <h1>Welcome to ${APP_CONFIG.APP_NAME}</h1>
                        <p>Discover amazing products at unbeatable prices</p>
                        <a href="${ROUTES.PRODUCTS}" class="btn btn-primary btn-large">Shop Now</a>
                    </div>
                </section>

                <section class="features">
                    <div class="feature">
                        <h3>🚚 Fast Delivery</h3>
                        <p>Quick and reliable shipping</p>
                    </div>
                    <div class="feature">
                        <h3>💰 Best Prices</h3>
                        <p>Competitive pricing on all products</p>
                    </div>
                    <div class="feature">
                        <h3>🔒 Secure Payment</h3>
                        <p>Safe and secure payment options</p>
                    </div>
                    <div class="feature">
                        <h3>📞 Customer Support</h3>
                        <p>24/7 customer service</p>
                    </div>
                </section>
            </div>
        `;
    }
}

// Initialize app - handle both DOMContentLoaded and immediate loading
if (document.readyState === 'loading') {
    console.log('⏳ DOM is loading, waiting for DOMContentLoaded event...');
    document.addEventListener('DOMContentLoaded', () => {
        console.log('✅ DOMContentLoaded event fired');
        App.init();
    });
} else {
    console.log('✅ DOM already loaded, initializing app immediately');
    App.init();
}

// Export App for console debugging
window.App = App;
console.log('📱 App ready! You can type "App" in console to access it');
