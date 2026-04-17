// ============================================
// PAYMENT SERVICE - ES6 Module
// ============================================

import { API_CONFIG } from '../config.js';
import { StorageService } from '../utils/storage.js';

export class PaymentService {
    static async generatePaymentLink(paymentData) {
        try {
            const token = StorageService.getToken();
            console.log('Generating payment link for order:', paymentData.orderId);
            console.log('Payment data:', paymentData);

            const response = await fetch(`${API_CONFIG.PAYMENT_SERVICE}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    orderId: paymentData.orderId,
                    amount: paymentData.amount,
                    phoneNumber: paymentData.phoneNumber,
                    name: paymentData.name,
                    email: paymentData.email,
                    paymentGatewayType: paymentData.gateway
                }),
                credentials: 'include'
            });

            if (!response.ok) {
                const errorText = await response.text();
                console.error('Payment API error response:', response.status, errorText);
                throw new Error(`Failed to generate payment link: ${response.status} - ${errorText}`);
            }

            const paymentLink = await response.text();
            console.log('✅ Payment link generated:', paymentLink);
            return { success: true, paymentLink };

        } catch (error) {
            console.error('❌ Error generating payment link:', error);
            return { success: false, error: error.message };
        }
    }

    static openPaymentGateway(paymentLink) {
        console.log('Opening payment gateway:', paymentLink);
        window.open(paymentLink, '_blank');
    }
}
