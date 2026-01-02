package com.ute.foodiedash.interfaces.rest.paymentmethod;

import com.ute.foodiedash.application.paymentmethod.command.CreatePaymentMethodCommand;
import com.ute.foodiedash.application.paymentmethod.command.UpdatePaymentMethodCommand;
import com.ute.foodiedash.application.paymentmethod.query.PaymentMethodQueryResult;
import com.ute.foodiedash.application.paymentmethod.usecase.CreatePaymentMethodUseCase;
import com.ute.foodiedash.application.paymentmethod.usecase.UpdatePaymentMethodUseCase;
import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.CreatePaymentMethodRequestDTO;
import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.PaymentMethodResponseDTO;
import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.UpdatePaymentMethodRequestDTO;
import com.ute.foodiedash.interfaces.rest.paymentmethod.mapper.PaymentMethodDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/payment-methods")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentMethodController {
    private final CreatePaymentMethodUseCase createPaymentMethodUseCase;
    private final UpdatePaymentMethodUseCase updatePaymentMethodUseCase;
    private final PaymentMethodDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<PaymentMethodResponseDTO> create(@RequestBody CreatePaymentMethodRequestDTO dto) {
        CreatePaymentMethodCommand command = dtoMapper.toCommand(dto);
        PaymentMethodQueryResult result = createPaymentMethodUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toResponseDto(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodResponseDTO> update(@PathVariable Long id,
                                                           @RequestBody UpdatePaymentMethodRequestDTO dto) {
        UpdatePaymentMethodCommand command = dtoMapper.toCommand(dto);
        PaymentMethodQueryResult result = updatePaymentMethodUseCase.execute(id, command);
        return ResponseEntity.ok(dtoMapper.toResponseDto(result));
    }
}

