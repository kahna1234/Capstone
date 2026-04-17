export class Validators {
    static isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    static isValidPassword(password) {
        // Minimum 8 characters, at least one uppercase, one lowercase, one number
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
        return passwordRegex.test(password);
    }

    static isValidName(name) {
        return name && name.trim().length >= 2;
    }

    static isValidPhoneNumber(phone) {
        const phoneRegex = /^[0-9]{10}$/;
        return phoneRegex.test(phone.replace(/\D/g, ''));
    }

    static validateSignupForm(name, email, password, confirmPassword) {
        const errors = [];

        if (!this.isValidName(name)) {
            errors.push('Name must be at least 2 characters long');
        }

        if (!this.isValidEmail(email)) {
            errors.push('Please enter a valid email address');
        }

        if (!this.isValidPassword(password)) {
            errors.push('Password must be at least 8 characters with uppercase, lowercase, and numbers');
        }

        if (password !== confirmPassword) {
            errors.push('Passwords do not match');
        }

        return { isValid: errors.length === 0, errors };
    }

    static validateLoginForm(email, password) {
        const errors = [];

        if (!this.isValidEmail(email)) {
            errors.push('Please enter a valid email address');
        }

        if (!password || password.length === 0) {
            errors.push('Password is required');
        }

        return { isValid: errors.length === 0, errors };
    }

    static validateCheckoutForm(firstName, lastName, email, phone, address) {
        const errors = [];

        if (!this.isValidName(firstName)) {
            errors.push('First name is required');
        }

        if (!this.isValidName(lastName)) {
            errors.push('Last name is required');
        }

        if (!this.isValidEmail(email)) {
            errors.push('Please enter a valid email');
        }

        if (!this.isValidPhoneNumber(phone)) {
            errors.push('Please enter a valid 10-digit phone number');
        }

        if (!address || address.trim().length < 5) {
            errors.push('Please enter a valid address');
        }

        return { isValid: errors.length === 0, errors };
    }
}

