package com.ute.foodiedash.interfaces.rest.order.mapper;

import com.ute.foodiedash.application.order.command.CheckoutOrderCommand;
import com.ute.foodiedash.application.order.query.CheckoutOrderResult;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderRequestDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    CheckoutOrderCommand toCommand(CheckoutOrderRequestDTO dto);

    CheckoutOrderResponseDTO toResponseDto(CheckoutOrderResult result);
}
