package com.ute.foodiedash.domain.paymentmethod.repository;

import com.ute.foodiedash.domain.paymentmethod.model.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository {
    PaymentMethod save(PaymentMethod paymentMethod);

    Optional<PaymentMethod> findById(Long id);

    Optional<PaymentMethod> findByCode(String code);

    boolean existsByCode(String code);

    List<PaymentMethod> findAllActive();
}

