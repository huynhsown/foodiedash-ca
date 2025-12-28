package com.ute.foodiedash.interfaces.rest.order.mapper;

import com.ute.foodiedash.application.order.query.CoordinateResult;
import com.ute.foodiedash.application.order.query.DeliveryResult;
import com.ute.foodiedash.application.order.query.ItemResult;
import com.ute.foodiedash.application.order.query.OptionResult;
import com.ute.foodiedash.application.order.query.OptionValueResult;
import com.ute.foodiedash.application.order.query.PaymentResult;
import com.ute.foodiedash.application.order.query.PickupOrderResult;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderDetailResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PickupOrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PickupOrderDtoMapper {

    PickupOrderResponseDTO toResponseDto(PickupOrderResult result);

    @Mapping(target = "unitPrice", source = "unitPrice", qualifiedByName = "bigDecimalToMoney")
    @Mapping(target = "totalPrice", source = "totalPrice", qualifiedByName = "bigDecimalToMoney")
    @Mapping(target = "options", source = "options")
    OrderDetailResponseDTO.ItemDTO toItemDto(ItemResult item);

    @Mapping(target = "values", source = "values")
    OrderDetailResponseDTO.OptionDTO toOptionDto(OptionResult option);

    @Mapping(target = "extraPrice", source = "extraPrice", qualifiedByName = "bigDecimalToMoney")
    OrderDetailResponseDTO.OptionValueDTO toOptionValueDto(OptionValueResult value);

    @Mapping(target = "lat", source = "lat", qualifiedByName = "bigDecimalToDouble")
    @Mapping(target = "lng", source = "lng", qualifiedByName = "bigDecimalToDouble")
    @Mapping(target = "distanceKm", source = "distanceKm", qualifiedByName = "bigDecimalToDouble")
    @Mapping(target = "geometry", source = "geometry")
    OrderDetailResponseDTO.DeliveryDTO toDeliveryDto(DeliveryResult delivery);

    @Mapping(target = "lat", source = "lat", qualifiedByName = "bigDecimalToDouble")
    @Mapping(target = "lng", source = "lng", qualifiedByName = "bigDecimalToDouble")
    OrderDetailResponseDTO.CoordinateDTO toCoordinateDto(CoordinateResult coordinate);

    OrderDetailResponseDTO.PaymentDTO toPaymentDto(PaymentResult payment);

    @org.mapstruct.Named("bigDecimalToMoney")
    default Long bigDecimalToMoney(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(0, RoundingMode.HALF_UP).longValue();
    }

    @org.mapstruct.Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }
}
