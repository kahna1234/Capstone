// ============================================
// API CONFIGURATION - ES6 Module Export
// ============================================

function normalizeBaseUrl(baseUrl) {
    if (!baseUrl) return '';
    return String(baseUrl).replace(/\/+$/, '');
}

/**
 * Runtime API base URL resolution order:
 * 1) window.__RUNTIME_CONFIG__.API_BASE_URL (if provided by hosting layer)
 * 2) localStorage.API_BASE_URL (useful for ngrok / dev setups)
 * 3) empty string => same-origin (best for podman/nginx deployments)
 */
export function getApiBaseUrl() {
    try {
        const fromWindow = globalThis?.__RUNTIME_CONFIG__?.API_BASE_URL;
        if (fromWindow) return normalizeBaseUrl(fromWindow);
    } catch (_) {
        // ignore
    }

    try {
        const fromStorage = globalThis?.localStorage?.getItem('API_BASE_URL');
        if (fromStorage) return normalizeBaseUrl(fromStorage);
    } catch (_) {
        // ignore
    }

    return '';
}

const API_BASE_URL = getApiBaseUrl();

export const API_CONFIG = {
    AUTH_SERVICE: `${API_BASE_URL}/auth`,
    PRODUCT_SERVICE: `${API_BASE_URL}/products`,
    ORDER_SERVICE: `${API_BASE_URL}/orders`,
    PAYMENT_SERVICE: `${API_BASE_URL}/api/payments`,
    SEARCH_SERVICE: `${API_BASE_URL}/search`,
    INVENTORY_SERVICE: `${API_BASE_URL}/inventory`,
    CART_SERVICE: `${API_BASE_URL}/cart`,
};

export const APP_CONFIG = {
    APP_NAME: 'ECommerce Store',
    ITEMS_PER_PAGE: 12,
    TIMEOUT: 30000,
};

export const ROUTES = {
    HOME: '#home',
    PRODUCTS: '#products',
    PRODUCT_DETAIL: '#product-detail',
    CART: '#cart',
    CHECKOUT: '#checkout',
    ORDERS: '#orders',
    LOGIN: '#login',
    SIGNUP: '#signup',
    PAYMENT_SUCCESS: '#payment-success',
};

