package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.OrderPayment;

import java.time.Instant;

public record PaymentResult(
        String paymentMethodCode,
        String paymentStatus,
        String transactionId,
        Instant paidAt,
        Instant refundedAt
) {
    public static PaymentResult from(OrderPayment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentResult(
                payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null,
                payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null,
                payment.getTransactionId(),
                payment.getPaidAt(),
                payment.getRefundedAt()
        );
    }
}
