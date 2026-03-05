package com.ute.foodiedash.interfaces.rest.promotion.dto;

import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import com.ute.foodiedash.interfaces.rest.common.dto.PageRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class PromotionListRequestDTO extends PageRequestDTO {
    private String code;
    private String name;
    private PromotionStatus status;
    private PromotionType type;
    private EligibilityRule eligibilityRule;
    private LocalDateTime startFrom;
    private LocalDateTime startTo;
    private LocalDateTime endFrom;
    private LocalDateTime endTo;
    private Boolean deleted;
}
