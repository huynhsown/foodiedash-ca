package com.ute.foodiedash.application.paymentmethod.usecase;

import com.ute.foodiedash.application.paymentmethod.query.PaymentMethodQueryResult;
import com.ute.foodiedash.domain.paymentmethod.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListActivePaymentMethodsUseCase {
    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional(readOnly = true)
    public List<PaymentMethodQueryResult> execute() {
        return paymentMethodRepository.findAllActive()
                .stream()
                .map(PaymentMethodQueryResult::from)
                .toList();
    }
}

