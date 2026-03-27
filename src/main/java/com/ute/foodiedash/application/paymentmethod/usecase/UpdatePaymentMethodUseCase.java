package com.ute.foodiedash.application.paymentmethod.usecase;

import com.ute.foodiedash.application.paymentmethod.command.UpdatePaymentMethodCommand;
import com.ute.foodiedash.application.paymentmethod.query.PaymentMethodQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.paymentmethod.model.PaymentMethod;
import com.ute.foodiedash.domain.paymentmethod.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdatePaymentMethodUseCase {
    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public PaymentMethodQueryResult execute(Long id, UpdatePaymentMethodCommand command) {
        PaymentMethod pm = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment method not found"));

        if (command.name() != null) {
            pm.rename(command.name());
        }
        if (command.type() != null) {
            pm.changeType(command.type());
        }
        if (command.active() != null) {
            if (command.active()) {
                pm.activate();
            } else {
                pm.deactivate();
            }
        }

        return PaymentMethodQueryResult.from(paymentMethodRepository.save(pm));
    }
}

