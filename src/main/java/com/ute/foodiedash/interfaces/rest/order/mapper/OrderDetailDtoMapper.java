package com.ute.foodiedash.interfaces.rest.order.mapper;

import com.ute.foodiedash.application.order.query.OrderDetailQueryResult;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderDetailResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDetailDtoMapper {
    OrderDetailResponseDTO toResponseDto(OrderDetailQueryResult result);

    default Long toMoney(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(0, RoundingMode.HALF_UP).longValue();
    }

    default Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }
}
