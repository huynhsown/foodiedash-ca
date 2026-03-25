package com.ute.foodiedash.interfaces.rest.review;

import com.ute.foodiedash.application.reviews.command.ReplyToReviewCommand;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.application.reviews.usecase.ReplyToReviewUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.review.dto.ReplyToReviewRequestDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.ReviewResponseDTO;
import com.ute.foodiedash.interfaces.rest.review.mapper.ReviewDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class MerchantReviewController {

    private final ReplyToReviewUseCase replyToReviewUseCase;
    private final ReviewDtoMapper reviewDtoMapper;

    @PostMapping("/{reviewId}/reply")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<ReviewResponseDTO> reply(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReplyToReviewRequestDTO dto
    ) {
        Long merchantId = SecurityContextHelper.getCurrentUserId();

        ReplyToReviewCommand command = new ReplyToReviewCommand(reviewId, merchantId, dto.getReply());
        ReviewQueryResult result = replyToReviewUseCase.execute(command);

        return ResponseEntity.ok(reviewDtoMapper.toResponseDto(result));
    }
}
