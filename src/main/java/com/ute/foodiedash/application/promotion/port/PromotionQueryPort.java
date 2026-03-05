package com.ute.foodiedash.application.promotion.port;

import com.ute.foodiedash.application.promotion.query.PromotionQueryResult;
import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PromotionQueryPort {
    Page<PromotionQueryResult> search(
        String code,
        PromotionStatus status,
        PromotionType type,
        EligibilityRule eligibilityRule,
        String name,
        LocalDateTime startFrom,
        LocalDateTime startTo,
        LocalDateTime endFrom,
        LocalDateTime endTo,
        Boolean deleted,
        Pageable pageable
    );
}
