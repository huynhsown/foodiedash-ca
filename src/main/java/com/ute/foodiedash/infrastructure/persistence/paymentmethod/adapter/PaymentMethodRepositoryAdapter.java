package com.ute.foodiedash.infrastructure.persistence.paymentmethod.adapter;

import com.ute.foodiedash.domain.paymentmethod.model.PaymentMethod;
import com.ute.foodiedash.domain.paymentmethod.repository.PaymentMethodRepository;
import com.ute.foodiedash.infrastructure.persistence.paymentmethod.jpa.entity.PaymentMethodJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.paymentmethod.jpa.mapper.PaymentMethodJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.paymentmethod.jpa.repository.PaymentMethodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentMethodRepositoryAdapter implements PaymentMethodRepository {
    private final PaymentMethodJpaRepository jpaRepository;
    private final PaymentMethodJpaMapper mapper;

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        PaymentMethodJpaEntity jpaEntity;
        if (paymentMethod.getId() == null) {
            jpaEntity = mapper.toJpaEntity(paymentMethod);
        } else {
            jpaEntity = jpaRepository.findById(paymentMethod.getId())
                    .orElseThrow();
            mapper.updateEntity(jpaEntity, paymentMethod);
        }
        PaymentMethodJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PaymentMethod> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<PaymentMethod> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public List<PaymentMethod> findAllActive() {
        return jpaRepository.findAllActive().stream().map(mapper::toDomain).toList();
    }
}

