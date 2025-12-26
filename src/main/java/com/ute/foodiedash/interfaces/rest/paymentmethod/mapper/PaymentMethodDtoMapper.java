package com.ute.foodiedash.interfaces.rest.paymentmethod.mapper;

import com.ute.foodiedash.application.paymentmethod.command.CreatePaymentMethodCommand;
import com.ute.foodiedash.application.paymentmethod.command.UpdatePaymentMethodCommand;
import com.ute.foodiedash.application.paymentmethod.query.PaymentMethodQueryResult;
import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.CreatePaymentMethodRequestDTO;
import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.PaymentMethodResponseDTO;
import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.UpdatePaymentMethodRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodDtoMapper {
    CreatePaymentMethodCommand toCommand(CreatePaymentMethodRequestDTO dto);

    UpdatePaymentMethodCommand toCommand(UpdatePaymentMethodRequestDTO dto);

    PaymentMethodResponseDTO toResponseDto(PaymentMethodQueryResult result);
}

