package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.order.enums.PaymentStatus;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import lombok.Getter;

import java.time.Instant;

@Getter
public class OrderPayment extends BaseEntity {
    private Long id;
    private Long orderId;
    private PaymentMethodCode paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private Instant paidAt;
    private Instant refundedAt;

    public static OrderPayment create(Long orderId, PaymentMethodCode paymentMethod) {
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
            PaymentMethodCode paymentMethod,
            PaymentStatus paymentStatus,
            String transactionId,
            Instant paidAt,
            Instant refundedAt,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        OrderPayment payment = new OrderPayment();

        payment.id = id;
        payment.orderId = orderId;
        payment.paymentMethod = paymentMethod;
        payment.paymentStatus = paymentStatus;
        payment.transactionId = transactionId;
        payment.paidAt = paidAt;
        payment.refundedAt = refundedAt;

        payment.restoreAudit(
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                deletedAt,
                version
        );

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

    public void ensurePaidWhenOrderCompleted(String orderCode) {
        if (orderCode == null || orderCode.isBlank()) {
            throw new BadRequestException("Order code required");
        }
        if (paymentStatus == PaymentStatus.PAID) {
            return;
        }
        if (paymentStatus != PaymentStatus.PENDING) {
            throw new BadRequestException("Cannot complete order with this payment status");
        }
        if (paymentMethod != PaymentMethodCode.COD) {
            throw new BadRequestException("Payment must be completed before delivery can be finished");
        }
        markPaid("COD-" + orderCode);
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
