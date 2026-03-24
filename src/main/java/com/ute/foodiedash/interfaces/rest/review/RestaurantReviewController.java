package com.ute.foodiedash.interfaces.rest.review;

import com.ute.foodiedash.application.reviews.query.RestaurantReviewsQueryResult;
import com.ute.foodiedash.application.reviews.usecase.GetRestaurantReviewsUseCase;
import com.ute.foodiedash.interfaces.rest.review.dto.RestaurantReviewsResponseDTO;
import com.ute.foodiedash.interfaces.rest.review.mapper.ReviewDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantReviewController {

    private final GetRestaurantReviewsUseCase getRestaurantReviewsUseCase;
    private final ReviewDtoMapper reviewDtoMapper;

    @GetMapping("/{restaurantId}/reviews")
    public ResponseEntity<RestaurantReviewsResponseDTO> getReviewsByRestaurantId(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        RestaurantReviewsQueryResult result = getRestaurantReviewsUseCase.execute(restaurantId, limit, offset);
        RestaurantReviewsResponseDTO response = reviewDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }
}
