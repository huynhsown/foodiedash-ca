package com.ute.foodiedash.interfaces.rest.paymentmethod;

import com.ute.foodiedash.application.paymentmethod.usecase.ListActivePaymentMethodsUseCase;
import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.PaymentMethodResponseDTO;
import com.ute.foodiedash.interfaces.rest.paymentmethod.mapper.PaymentMethodDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final ListActivePaymentMethodsUseCase listActivePaymentMethodsUseCase;
    private final PaymentMethodDtoMapper dtoMapper;

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponseDTO>> listActive() {
        var results = listActivePaymentMethodsUseCase.execute();
        return ResponseEntity.ok(results.stream().map(dtoMapper::toResponseDto).toList());
    }
}

