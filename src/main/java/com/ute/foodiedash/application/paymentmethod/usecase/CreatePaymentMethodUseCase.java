package com.ute.foodiedash.application.paymentmethod.usecase;

import com.ute.foodiedash.application.paymentmethod.command.CreatePaymentMethodCommand;
import com.ute.foodiedash.application.paymentmethod.query.PaymentMethodQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodType;
import com.ute.foodiedash.domain.paymentmethod.model.PaymentMethod;
import com.ute.foodiedash.domain.paymentmethod.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreatePaymentMethodUseCase {
    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public PaymentMethodQueryResult execute(CreatePaymentMethodCommand command) {
        if (command.code() == null || command.code().isBlank()) {
            throw new BadRequestException("Payment method code required");
        }
        String code = command.code().trim().toUpperCase();
        if (paymentMethodRepository.existsByCode(code)) {
            throw new BadRequestException("Payment method code already exists");
        }

        PaymentMethodType type = command.type();
        boolean active = command.active() == null || command.active();

        PaymentMethod pm = PaymentMethod.create(code, command.name(), type, active);
        return PaymentMethodQueryResult.from(paymentMethodRepository.save(pm));
    }
}

