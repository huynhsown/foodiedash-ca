package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.order.enums.PaymentMethod;
import com.ute.foodiedash.domain.order.enums.PaymentStatus;
import lombok.Getter;

import java.time.Instant;

@Getter
public class OrderPayment extends BaseEntity {
    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private Instant paidAt;
    private Instant refundedAt;

    public static OrderPayment create(Long orderId, PaymentMethod paymentMethod) {
        if (orderId == null) {
            throw new BadRequestException("Order id required");
        }

        if (paymentMethod == null) {
            throw new BadRequestException("Payment method required");
        }

        OrderPayment payment = new OrderPayment();
        payment.orderId = orderId;
        payment.paymentMethod = paymentMethod;
        payment.paymentStatus = PaymentStatus.PENDING;

        return payment;
    }

    public static OrderPayment reconstruct(
            Long id,
            Long orderId,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            String transactionId,
            Instant paidAt,
            Instant refundedAt
    ) {
        OrderPayment payment = new OrderPayment();

        payment.id = id;
        payment.orderId = orderId;
        payment.paymentMethod = paymentMethod;
        payment.paymentStatus = paymentStatus;
        payment.transactionId = transactionId;
        payment.paidAt = paidAt;
        payment.refundedAt = refundedAt;

        return payment;
    }

    public void markPaid(String transactionId) {

        if (paymentStatus == PaymentStatus.PAID) return;


        if (paymentStatus != PaymentStatus.PENDING) {
            throw new BadRequestException("Invalid payment state");
        }

        if (transactionId == null || transactionId.isBlank()) {
            throw new BadRequestException("Transaction id required");
        }

        this.paymentStatus = PaymentStatus.PAID;
        this.transactionId = transactionId;
        this.paidAt = Instant.now();
    }

    public void markFailed() {

        if (paymentStatus != PaymentStatus.PENDING) {
            throw new BadRequestException("Only pending payment can fail");
        }

        this.paymentStatus = PaymentStatus.FAILED;
    }

    public void markRefunded() {

        if (paymentStatus != PaymentStatus.PAID) {
            throw new BadRequestException("Only paid order can be refunded");
        }

        this.paymentStatus = PaymentStatus.REFUNDED;
        this.refundedAt = Instant.now();
    }

    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }


    public boolean isFailed() {
        return paymentStatus == PaymentStatus.FAILED;
    }

    public boolean isRefunded() {
        return paymentStatus == PaymentStatus.REFUNDED;
    }
}
