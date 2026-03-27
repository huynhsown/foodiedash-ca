package com.ute.foodiedash.application.payment.vnpay.usecase;

import com.ute.foodiedash.application.payment.vnpay.command.HandleVnpayReturnCommand;
import com.ute.foodiedash.application.payment.vnpay.port.VnpaySignatureVerifierPort;
import com.ute.foodiedash.application.payment.vnpay.query.VnpayReturnResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.order.model.OrderPayment;
import com.ute.foodiedash.domain.order.repository.OrderPaymentRepository;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HandleVnpayReturnUseCase {
    private final VnpaySignatureVerifierPort signatureVerifierPort;
    private final OrderRepository orderRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    @Transactional
    public VnpayReturnResult execute(HandleVnpayReturnCommand command) {
        if (command == null || command.params() == null) {
            throw new BadRequestException("Missing vnpay params");
        }

        Map<String, String> params = command.params();

        if (!signatureVerifierPort.verifySecureHash(params)) {
            throw new BadRequestException("Invalid vnpay signature");
        }

        String orderCode = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionNo = params.get("vnp_TransactionNo");

        if (orderCode == null || orderCode.isBlank()) {
            throw new BadRequestException("Missing vnp_TxnRef");
        }
        if (responseCode == null || responseCode.isBlank()) {
            throw new BadRequestException("Missing vnp_ResponseCode");
        }

        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(() -> new NotFoundException("Order not found with code: " + orderCode));

        OrderPayment payment = orderPaymentRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new NotFoundException("Order payment not found for orderId: " + order.getId()));

        // VNPAY success code is typically "00"
        if ("00".equals(responseCode)) {
            String txId = (transactionNo != null && !transactionNo.isBlank()) ? transactionNo : orderCode;
            payment.markPaid(txId);
            orderPaymentRepository.save(payment);
            return new VnpayReturnResult(true, "Payment success");
        }

        // Failure path
        payment.markFailed();
        orderPaymentRepository.save(payment);
        return new VnpayReturnResult(false, "Payment failed: " + responseCode);
    }
}

