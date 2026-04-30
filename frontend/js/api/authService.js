// ============================================
// AUTHENTICATION SERVICE - ES6 Module
// ============================================

import { API_CONFIG } from '../config.js';
import { StorageService } from '../utils/storage.js';

export class AuthService {
    static async signup(name, email, password) {
        try {
            console.log('Signup API called:', {
                url: `${API_CONFIG.AUTH_SERVICE}/signup`,
                name,
                email
            });

            const response = await fetch(`${API_CONFIG.AUTH_SERVICE}/signup`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name, email, password }),
                credentials: 'include'  // Include cookies/credentials
            });

            console.log('Signup response status:', response.status);

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                let errorMessage = `Signup failed: ${response.status}`;
                
                // Handle validation errors from @Valid annotations
                if (response.status === 400) {
                    if (errorData.errors && Array.isArray(errorData.errors)) {
                        errorMessage = errorData.errors.join(', ');
                    } else if (errorData.message) {
                        errorMessage = errorData.message;
                    } else if (errorData.error) {
                        errorMessage = errorData.error;
                    }
                } else if (errorData.error) {
                    errorMessage = errorData.error;
                }
                
                console.error('Signup error:', errorMessage);
                throw new Error(errorMessage);
            }

            const user = await response.json();
            console.log('Signup successful:', user);

            // Token is sent as HttpOnly cookie by backend — browser handles it automatically

            StorageService.setUser(user);
            return { success: true, user };

        } catch (error) {
            console.error('Signup error:', error);
            return { success: false, error: error.message };
        }
    }

    static async login(email, password) {
        try {
            console.log('Login API called:', {
                url: `${API_CONFIG.AUTH_SERVICE}/login`,
                email
            });

            const response = await fetch(`${API_CONFIG.AUTH_SERVICE}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
                credentials: 'include'  // Include cookies/credentials
            });

            console.log('Login response status:', response.status);

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                let errorMessage = `Login failed: ${response.status}`;
                
                // Handle validation errors from @Valid annotations
                if (response.status === 400) {
                    if (errorData.errors && Array.isArray(errorData.errors)) {
                        errorMessage = errorData.errors.join(', ');
                    } else if (errorData.message) {
                        errorMessage = errorData.message;
                    } else if (errorData.error) {
                        errorMessage = errorData.error;
                    }
                } else if (errorData.error) {
                    errorMessage = errorData.error;
                }
                
                console.error('Login error:', errorMessage);
                throw new Error(errorMessage);
            }

            const user = await response.json();
            console.log('Login successful:', user);

            // Token is sent as HttpOnly cookie by backend — browser handles it automatically

            StorageService.setUser(user);
            return { success: true, user };

        } catch (error) {
            console.error('Login error:', error);
            return { success: false, error: error.message };
        }
    }

    static async validateToken(token) {
        try {
            console.log('Validate token API called');

            const response = await fetch(`${API_CONFIG.AUTH_SERVICE}/validate`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ token }),
                credentials: 'include'  // Include cookies/credentials
            });

            console.log('Token validation response:', response.status);
            return response.ok;

        } catch (error) {
            console.error('Token validation error:', error);
            return false;
        }
    }

    static async logout() {
        try {
            console.log('Logout API called');
            await fetch(`${API_CONFIG.AUTH_SERVICE}/logout`, {
                method: 'POST',
                credentials: 'include'
            });
        } catch (error) {
            console.error('Logout error:', error);
        }
        StorageService.logout();
    }

    static isLoggedIn() {
        return StorageService.isLoggedIn();
    }

    static getCurrentUser() {
        return StorageService.getUser();
    }
}
