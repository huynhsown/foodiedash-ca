package com.ute.foodiedash.interfaces.rest.promotion.mapper;

import com.ute.foodiedash.application.promotion.command.*;
import com.ute.foodiedash.application.promotion.query.*;
import com.ute.foodiedash.interfaces.rest.promotion.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionDtoMapper {

    CreatePromotionCommand toCommand(CreatePromotionRequestDTO dto);
    UpdatePromotionCommand toCommand(UpdatePromotionRequestDTO dto);
    ChangePromotionStatusCommand toCommand(ChangePromotionStatusRequestDTO dto);
    AddPromotionRestaurantsCommand toCommand(AddPromotionRestaurantsRequestDTO dto);
    ReservePromotionCommand toCommand(ReservePromotionRequestDTO dto);
    ConfirmPromotionUsageCommand toCommand(ConfirmPromotionRequestDTO dto);
    ReleasePromotionCommand toCommand(ReleasePromotionRequestDTO dto);

    PromotionResponseDTO toResponseDto(PromotionQueryResult result);
    PromotionReservationResponseDTO toResponseDto(PromotionReservationQueryResult result);
    PromotionRestaurantsResponseDTO toResponseDto(PromotionRestaurantsQueryResult result);
    PromotionDiscountResponseDTO toResponseDto(PromotionDiscountQueryResult result);
    PromotionEligibilityResponseDTO toResponseDto(PromotionEligibilityQueryResult result);
}
