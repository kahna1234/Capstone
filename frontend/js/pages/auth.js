// ============================================
// AUTH PAGE - ES6 Module
// ============================================

import { AuthService } from '../api/authService.js';
import { Validators } from '../utils/validators.js';
import { Helpers } from '../utils/helpers.js';
import { ROUTES } from '../config.js';

export class AuthPage {
    static renderLogin() {
        const container = document.getElementById('app-container');
        container.innerHTML = `
            <div class="auth-container">
                <div class="auth-form">
                    <h2>Login</h2>
                    <form id="login-form">
                        <div class="form-group">
                            <label for="login-email">Email:</label>
                            <input type="email" id="login-email" required>
                        </div>
                        <div class="form-group">
                            <label for="login-password">Password:</label>
                            <input type="password" id="login-password" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Login</button>
                    </form>
                    <p>Don't have an account? <a href="${ROUTES.SIGNUP}">Sign up here</a></p>
                </div>
            </div>
        `;

        document.getElementById('login-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleLogin();
        });
    }

    static renderSignup() {
        const container = document.getElementById('app-container');
        container.innerHTML = `
            <div class="auth-container">
                <div class="auth-form">
                    <h2>Sign Up</h2>
                    <form id="signup-form">
                        <div class="form-group">
                            <label for="signup-name">Full Name:</label>
                            <input type="text" id="signup-name" required minlength="2" maxlength="100">
                        </div>
                        <div class="form-group">
                            <label for="signup-email">Email:</label>
                            <input type="email" id="signup-email" required>
                        </div>
                        <div class="form-group">
                            <label for="signup-password">Password:</label>
                            <input type="password" id="signup-password" required minlength="8">
                            <small class="password-hint">Password must be at least 8 characters with uppercase, lowercase, and a digit</small>
                        </div>
                        <div class="form-group">
                            <label for="signup-confirm-password">Confirm Password:</label>
                            <input type="password" id="signup-confirm-password" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Sign Up</button>
                    </form>
                    <p>Already have an account? <a href="${ROUTES.LOGIN}">Login here</a></p>
                </div>
            </div>
        `;

        document.getElementById('signup-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleSignup();
        });
    }

    static async handleLogin() {
        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;

        const validation = Validators.validateLoginForm(email, password);
        if (!validation.isValid) {
            validation.errors.forEach(error => Helpers.showError(error));
            return;
        }

        const result = await AuthService.login(email, password);
        if (result.success) {
            Helpers.showSuccess('Logged in successfully!');
            // Sync cart from server after login
            const { Navbar } = await import('../components/navbar.js');
            await Navbar.syncCartFromServer();
            // Render navbar to update login state
            Navbar.render();
            App.navigate(ROUTES.HOME);
        } else {
            Helpers.showError(result.error || 'Login failed');
        }
    }

    static async handleSignup() {
        const name = document.getElementById('signup-name').value;
        const email = document.getElementById('signup-email').value;
        const password = document.getElementById('signup-password').value;
        const confirmPassword = document.getElementById('signup-confirm-password').value;

        const validation = Validators.validateSignupForm(name, email, password, confirmPassword);
        if (!validation.isValid) {
            validation.errors.forEach(error => Helpers.showError(error));
            return;
        }

        const result = await AuthService.signup(name, email, password);
        if (result.success) {
            Helpers.showSuccess('Account created successfully!');
            // Render navbar to update login state
            const { Navbar } = await import('../components/navbar.js');
            Navbar.render();
            window.location.hash = ROUTES.LOGIN;
        } else {
            // Display backend validation errors
            if (result.error) {
                Helpers.showError(result.error);
            } else {
                Helpers.showError('Signup failed. Please try again.');
            }
        }
    }
}
