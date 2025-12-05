package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.command.AddPromotionRestaurantsCommand;
import com.ute.foodiedash.application.promotion.query.PromotionRestaurantsQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.model.PromotionRestaurant;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AddPromotionRestaurantsUseCase {
    private final PromotionRepository promotionRepository;
    private final PromotionRestaurantRepository promotionRestaurantRepository;

    @Transactional
    public PromotionRestaurantsQueryResult execute(Long promotionId, AddPromotionRestaurantsCommand command) {
        Promotion promotion = promotionRepository.findById(promotionId)
            .orElseThrow(() -> new NotFoundException("Promotion not found"));

        if (!promotion.isRestaurantSpecific()) {
            throw new BadRequestException("Only RESTAURANT_SPECIFIC promotions can have restaurants");
        }

        for (Long restaurantId : command.restaurantIds()) {
            boolean exists = promotionRestaurantRepository
                .existsByPromotionIdAndRestaurantIdAndNotDeleted(promotionId, restaurantId);

            if (!exists) {
                PromotionRestaurant pr = PromotionRestaurant.create(promotionId, restaurantId);
                promotionRestaurantRepository.save(pr);
            }
        }

        return new PromotionRestaurantsQueryResult(promotionId, command.restaurantIds());
    }
}
