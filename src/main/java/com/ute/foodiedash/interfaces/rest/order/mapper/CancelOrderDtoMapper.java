package com.ute.foodiedash.interfaces.rest.order.mapper;

import com.ute.foodiedash.application.order.command.CancelOrderCommand;
import com.ute.foodiedash.interfaces.rest.order.dto.CancelOrderRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CancelOrderDtoMapper {
    CancelOrderCommand toCommand(Long orderId, CancelOrderRequestDTO dto);
}

