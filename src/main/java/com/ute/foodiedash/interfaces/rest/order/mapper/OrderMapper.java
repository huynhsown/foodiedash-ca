package com.ute.foodiedash.interfaces.rest.order.mapper;

import com.ute.foodiedash.application.order.command.CheckoutOrderCommand;
import com.ute.foodiedash.application.order.command.PreviewOrderCommand;
import com.ute.foodiedash.application.order.query.CheckoutOrderResult;
import com.ute.foodiedash.application.order.query.PreviewOrderItemOptionResult;
import com.ute.foodiedash.application.order.query.PreviewOrderItemOptionValueResult;
import com.ute.foodiedash.application.order.query.PreviewOrderItemResult;
import com.ute.foodiedash.application.order.query.PreviewOrderResult;
import com.ute.foodiedash.application.order.query.PriceBreakdownResult;
import com.ute.foodiedash.application.promotion.query.PromotionPreviewResult;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderRequestDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PreviewOrderItemOptionDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PreviewOrderItemOptionValueDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PreviewOrderItemDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PreviewOrderRequestDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PreviewOrderResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PriceBreakdownDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PromotionPreviewDTO;
import com.ute.foodiedash.interfaces.rest.paymentmethod.mapper.PaymentMethodDtoMapper;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring", uses = PaymentMethodDtoMapper.class)
public interface OrderMapper {
    CheckoutOrderCommand toCommand(CheckoutOrderRequestDTO dto);

    CheckoutOrderResponseDTO toResponseDto(CheckoutOrderResult result);

    PreviewOrderCommand toPreviewCommand(PreviewOrderRequestDTO dto);

    PreviewOrderResponseDTO toPreviewResponseDto(PreviewOrderResult result);

    PromotionPreviewDTO toPromotionPreviewDto(PromotionPreviewResult result);

    PriceBreakdownDTO toPriceBreakdownDto(PriceBreakdownResult result);

    PreviewOrderItemDTO toPreviewOrderItemDto(PreviewOrderItemResult result);

    PreviewOrderItemOptionDTO toPreviewOrderItemOptionDto(PreviewOrderItemOptionResult result);

    PreviewOrderItemOptionValueDTO toPreviewOrderItemOptionValueDto(PreviewOrderItemOptionValueResult result);

    default Long toMoney(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(0, RoundingMode.HALF_UP).longValue();
    }
}
