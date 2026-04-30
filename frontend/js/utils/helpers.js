export class Helpers {
    static formatCurrency(amount) {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR'
        }).format(amount);
    }

    static formatDate(date) {
        return new Intl.DateTimeFormat('en-IN', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        }).format(new Date(date));
    }

    static showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;

        document.body.appendChild(notification);

        setTimeout(() => {
            notification.style.opacity = '1';
        }, 10);

        setTimeout(() => {
            notification.style.opacity = '0';
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    }

    static showError(message) {
        this.showNotification(message, 'error');
    }

    static showSuccess(message) {
        this.showNotification(message, 'success');
    }

    static showLoading(element) {
        element.innerHTML = '<div class="loader"></div>';
    }

    static generateOrderId() {
        return 'ORD-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
    }

    static delay(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    static deepClone(obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    // Handle API errors with appropriate user messages
    static async handleApiError(response) {
        const status = response.status;
        
        switch (status) {
            case 401:
                this.showError('Session expired. Please login again.');
                // Redirect to login after a short delay
                setTimeout(() => {
                    window.location.hash = '#login';
                }, 2000);
                throw new Error('Unauthorized');
            case 403:
                this.showError('Access denied. You don\'t have permission to perform this action.');
                throw new Error('Forbidden');
            case 429:
                this.showError('Too many requests. Please try again later.');
                throw new Error('Too Many Requests');
            case 400:
                const errorData = await response.json().catch(() => ({}));
                if (errorData.errors && Array.isArray(errorData.errors)) {
                    throw new Error(errorData.errors.join(', '));
                }
                throw new Error(errorData.message || errorData.error || 'Invalid request');
            case 500:
                this.showError('Server error. Please try again later.');
                throw new Error('Internal Server Error');
            default:
                throw new Error(`Request failed: ${status}`);
        }
    }
}

