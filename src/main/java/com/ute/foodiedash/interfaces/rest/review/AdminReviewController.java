package com.ute.foodiedash.interfaces.rest.review;

import com.ute.foodiedash.application.reviews.command.HideReviewCommand;
import com.ute.foodiedash.application.reviews.command.ShowReviewCommand;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.application.reviews.usecase.HideReviewUseCase;
import com.ute.foodiedash.application.reviews.usecase.ShowReviewUseCase;
import com.ute.foodiedash.interfaces.rest.review.dto.ReviewResponseDTO;
import com.ute.foodiedash.interfaces.rest.review.mapper.ReviewDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final HideReviewUseCase hideReviewUseCase;
    private final ShowReviewUseCase showReviewUseCase;
    private final ReviewDtoMapper reviewDtoMapper;

    @PatchMapping("/{reviewId}/hide")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponseDTO> hide(@PathVariable Long reviewId) {
        ReviewQueryResult result = hideReviewUseCase.execute(new HideReviewCommand(reviewId));
        return ResponseEntity.ok(reviewDtoMapper.toResponseDto(result));
    }

    @PatchMapping("/{reviewId}/show")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponseDTO> show(@PathVariable Long reviewId) {
        ReviewQueryResult result = showReviewUseCase.execute(new ShowReviewCommand(reviewId));
        return ResponseEntity.ok(reviewDtoMapper.toResponseDto(result));
    }
}
