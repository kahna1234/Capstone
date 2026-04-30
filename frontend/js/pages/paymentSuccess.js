// ============================================
// PAYMENT SUCCESS PAGE - ES6 Module
// ============================================

import { ROUTES, APP_CONFIG } from '../config.js';
import { Helpers } from '../utils/helpers.js';

export class PaymentSuccessPage {
    static render(orderId = '') {
        const container = document.getElementById('app-container');
        
        // Get order ID from URL params if not provided directly
        const hash = window.location.hash;
        const params = new URLSearchParams(hash.split('?')[1] || '');
        const orderIdFromUrl = params.get('orderId') || orderId;

        container.innerHTML = `
            <div class="payment-success-container">
                <div class="success-card">
                    <div class="success-icon">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <circle cx="12" cy="12" r="10" stroke="#4CAF50" stroke-width="2"/>
                            <path d="M8 12L11 15L16 9" stroke="#4CAF50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </div>
                    <h1>Payment Successful!</h1>
                    <p class="success-message">Thank you for your purchase. Your payment has been processed successfully.</p>
                    
                    ${orderIdFromUrl ? `
                        <div class="order-details">
                            <p><strong>Order ID:</strong> #${orderIdFromUrl}</p>
                            <p class="order-note">You will receive a confirmation email shortly.</p>
                        </div>
                    ` : ''}
                    
                    <div class="success-actions">
                        <a href="${ROUTES.ORDERS}" class="btn btn-primary">View My Orders</a>
                        <a href="${ROUTES.PRODUCTS}" class="btn btn-secondary">Continue Shopping</a>
                    </div>
                    
                    <div class="support-info">
                        <p>Need help? Contact our support team</p>
                        <p class="support-email">support@${APP_CONFIG.APP_NAME.toLowerCase().replace(/\s/g, '')}.com</p>
                    </div>
                </div>
            </div>
        `;

        // Add styles if not already present
        if (!document.getElementById('payment-success-styles')) {
            const style = document.createElement('style');
            style.id = 'payment-success-styles';
            style.textContent = `
                .payment-success-container {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    min-height: 70vh;
                    padding: 2rem;
                    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                }
                
                .success-card {
                    background: white;
                    border-radius: 16px;
                    padding: 3rem;
                    text-align: center;
                    max-width: 500px;
                    width: 100%;
                    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
                    animation: slideUp 0.5s ease-out;
                }
                
                @keyframes slideUp {
                    from {
                        opacity: 0;
                        transform: translateY(30px);
                    }
                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }
                
                .success-icon {
                    width: 80px;
                    height: 80px;
                    margin: 0 auto 1.5rem;
                }
                
                .success-icon svg {
                    width: 100%;
                    height: 100%;
                }
                
                .success-card h1 {
                    color: #4CAF50;
                    font-size: 2rem;
                    margin-bottom: 1rem;
                    font-weight: 600;
                }
                
                .success-message {
                    color: #666;
                    font-size: 1.1rem;
                    margin-bottom: 1.5rem;
                    line-height: 1.6;
                }
                
                .order-details {
                    background: #f8f9fa;
                    border-radius: 8px;
                    padding: 1.5rem;
                    margin: 1.5rem 0;
                    border-left: 4px solid #4CAF50;
                }
                
                .order-details p {
                    margin: 0.5rem 0;
                    color: #333;
                }
                
                .order-note {
                    font-size: 0.9rem;
                    color: #888;
                    font-style: italic;
                }
                
                .success-actions {
                    display: flex;
                    gap: 1rem;
                    justify-content: center;
                    margin-top: 2rem;
                    flex-wrap: wrap;
                }
                
                .success-actions .btn {
                    padding: 0.75rem 1.5rem;
                    font-size: 1rem;
                    text-decoration: none;
                }
                
                .btn-secondary {
                    background: transparent;
                    color: #333;
                    border: 2px solid #ddd;
                }
                
                .btn-secondary:hover {
                    background: #f5f5f5;
                    border-color: #999;
                }
                
                .support-info {
                    margin-top: 2rem;
                    padding-top: 1.5rem;
                    border-top: 1px solid #eee;
                }
                
                .support-info p {
                    color: #888;
                    font-size: 0.9rem;
                    margin: 0.25rem 0;
                }
                
                .support-email {
                    color: #333;
                    font-weight: 500;
                }
            `;
            document.head.appendChild(style);
        }

        Helpers.showSuccess('Payment completed successfully!');
    }
}
