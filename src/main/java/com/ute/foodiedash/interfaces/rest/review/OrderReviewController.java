package com.ute.foodiedash.interfaces.rest.review;

import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.application.reviews.usecase.GetOrderReviewUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.review.dto.ReviewResponseDTO;
import com.ute.foodiedash.interfaces.rest.review.mapper.ReviewDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderReviewController {

    private final GetOrderReviewUseCase getOrderReviewUseCase;
    private final ReviewDtoMapper reviewDtoMapper;

    @GetMapping("/{orderId}/review")
    public ResponseEntity<ReviewResponseDTO> getReview(
            @PathVariable Long orderId
    ) {
        Long customerId = SecurityContextHelper.getCurrentUserId();

        ReviewQueryResult result = getOrderReviewUseCase.execute(orderId, customerId);

        return ResponseEntity.ok(reviewDtoMapper.toResponseDto(result));
    }
}
