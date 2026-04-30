export class StorageService {
    static setToken(token) {
        localStorage.setItem('authToken', token);
    }

    static getToken() {
        return localStorage.getItem('authToken');
    }

    static removeToken() {
        localStorage.removeItem('authToken');
    }

    static setUser(user) {
        localStorage.setItem('user', JSON.stringify(user));
    }

    static getUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    }

    static removeUser() {
        localStorage.removeItem('user');
    }

    static setCart(cart) {
        localStorage.setItem('cart', JSON.stringify(cart));
    }

    static getCart() {
        const cart = localStorage.getItem('cart');
        return cart ? JSON.parse(cart) : [];
    }

    static addToCart(product) {
        const cart = this.getCart();
        const existingItem = cart.find(item => item.id === product.id);

        if (existingItem) {
            existingItem.quantity += product.quantity || 1;
        } else {
            cart.push({ ...product, quantity: product.quantity || 1 });
        }

        this.setCart(cart);
        return cart;
    }

    static removeFromCart(productId) {
        let cart = this.getCart();
        cart = cart.filter(item => item.id !== productId);
        this.setCart(cart);
        return cart;
    }

    static updateCartItem(productId, quantity) {
        const cart = this.getCart();
        const item = cart.find(item => item.id === productId);

        if (item) {
            if (quantity <= 0) {
                return this.removeFromCart(productId);
            }
            item.quantity = quantity;
            this.setCart(cart);
        }

        return cart;
    }

    static clearCart() {
        localStorage.removeItem('cart');
    }

    static isLoggedIn() {
        const user = this.getUser();
        console.log('[StorageService] isLoggedIn check:', !!user, user);
        return !!user; // token is HttpOnly cookie — check user object instead
    }

    static logout() {
        this.removeToken();
        this.removeUser();
        this.clearCart();
    }
}

