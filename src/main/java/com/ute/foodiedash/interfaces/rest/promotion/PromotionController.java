package com.ute.foodiedash.interfaces.rest.promotion;

import com.ute.foodiedash.application.promotion.command.*;
import com.ute.foodiedash.application.promotion.query.*;
import com.ute.foodiedash.application.promotion.usecase.*;
import com.ute.foodiedash.interfaces.rest.promotion.mapper.PromotionPageMapper;
import com.ute.foodiedash.interfaces.rest.common.dto.PageInfo;
import com.ute.foodiedash.interfaces.rest.promotion.dto.*;
import com.ute.foodiedash.interfaces.rest.promotion.mapper.PromotionDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final CreatePromotionUseCase createPromotionUseCase;
    private final UpdatePromotionUseCase updatePromotionUseCase;
    private final ChangePromotionStatusUseCase changePromotionStatusUseCase;
    private final AddPromotionRestaurantsUseCase addPromotionRestaurantsUseCase;
    private final ReservePromotionUseCase reservePromotionUseCase;
    private final ConfirmPromotionUsageUseCase confirmPromotionUsageUseCase;
    private final ReleasePromotionUseCase releasePromotionUseCase;
    private final ValidatePromotionEligibilityUseCase validatePromotionEligibilityUseCase;
    private final GetDiscountRuleUseCase getDiscountRuleUseCase;
    private final ListPromotionsUseCase listPromotionsUseCase;
    private final PromotionDtoMapper dtoMapper;
    private final PromotionPageMapper pageMapper;

    @PostMapping
    public ResponseEntity<PromotionResponseDTO> createPromotion(
            @Valid @RequestBody CreatePromotionRequestDTO dto) {
        CreatePromotionCommand command = dtoMapper.toCommand(dto);
        PromotionQueryResult result = createPromotionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toResponseDto(result));
    }

    @GetMapping
    public ResponseEntity<PageInfo<PromotionResponseDTO>> listPromotions(
            @Valid PromotionListRequestDTO filter) {
        PromotionPageResult pageResult = listPromotionsUseCase.execute(
            filter.getCode(), filter.getStatus(), filter.getType(),
            filter.getEligibilityRule(), filter.getName(),
            filter.getStartFrom(), filter.getStartTo(),
            filter.getEndFrom(), filter.getEndTo(),
            filter.getDeleted(), filter.toPageable()
        );
        return ResponseEntity.ok(pageMapper.toPageInfo(pageResult));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponseDTO> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePromotionRequestDTO dto) {
        UpdatePromotionCommand command = dtoMapper.toCommand(dto);
        PromotionQueryResult result = updatePromotionUseCase.execute(id, command);
        return ResponseEntity.ok(dtoMapper.toResponseDto(result));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PromotionResponseDTO> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangePromotionStatusRequestDTO dto) {
        ChangePromotionStatusCommand command = dtoMapper.toCommand(dto);
        PromotionQueryResult result = changePromotionStatusUseCase.execute(id, command);
        return ResponseEntity.ok(dtoMapper.toResponseDto(result));
    }

    @PostMapping("/{id}/restaurants")
    public ResponseEntity<PromotionRestaurantsResponseDTO> addRestaurants(
            @PathVariable Long id,
            @Valid @RequestBody AddPromotionRestaurantsRequestDTO dto) {
        AddPromotionRestaurantsCommand command = dtoMapper.toCommand(dto);
        PromotionRestaurantsQueryResult result = addPromotionRestaurantsUseCase.execute(id, command);
        return ResponseEntity.ok(dtoMapper.toResponseDto(result));
    }

    @PostMapping("/validate")
    public ResponseEntity<PromotionEligibilityResponseDTO> validatePromotion(
            @Valid @RequestBody EligibilityRequestDTO dto) {
        PromotionEligibilityQueryResult result = validatePromotionEligibilityUseCase.execute(
            dto.getPromotionCode(), dto.getUserId(),
            dto.getRestaurantId(), dto.getOrderSubtotal()
        );
        return ResponseEntity.ok(dtoMapper.toResponseDto(result));
    }

    @PostMapping("/discount-rule")
    public ResponseEntity<PromotionDiscountResponseDTO> getDiscountRule(
            @Valid @RequestBody DiscountRequestDTO dto) {
        PromotionDiscountQueryResult result = getDiscountRuleUseCase.execute(
            dto.getPromotionCode(), dto.getUserId(),
            dto.getRestaurantId(), dto.getOrderSubtotal()
        );
        return ResponseEntity.ok(dtoMapper.toResponseDto(result));
    }

    @PostMapping("/reserve")
    public ResponseEntity<PromotionReservationResponseDTO> reservePromotion(
            @Valid @RequestBody ReservePromotionRequestDTO dto) {
        ReservePromotionCommand command = dtoMapper.toCommand(dto);
        PromotionReservationQueryResult result = reservePromotionUseCase.execute(command);
        return ResponseEntity.ok(dtoMapper.toResponseDto(result));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPromotionUsage(
            @Valid @RequestBody ConfirmPromotionRequestDTO dto) {
        ConfirmPromotionUsageCommand command = dtoMapper.toCommand(dto);
        confirmPromotionUsageUseCase.execute(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/release")
    public ResponseEntity<Void> releasePromotion(
            @Valid @RequestBody ReleasePromotionRequestDTO dto) {
        ReleasePromotionCommand command = dtoMapper.toCommand(dto);
        releasePromotionUseCase.execute(command);
        return ResponseEntity.ok().build();
    }
}
