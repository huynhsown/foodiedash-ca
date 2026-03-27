package com.ute.foodiedash.interfaces.rest.order.mapper;

import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.query.OrderSummariesQueryResult;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderSummaryResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderSummariesResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderSummaryDtoMapper {

    OrderSummariesResponseDTO toResponseDto(OrderSummariesQueryResult result);

    OrderSummaryResponseDTO toResponseDto(OrderSummaryQueryResult result);

    default Long toMoney(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(0, RoundingMode.HALF_UP).longValue();
    }
}

