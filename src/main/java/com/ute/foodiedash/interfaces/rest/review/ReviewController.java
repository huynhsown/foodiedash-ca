package com.ute.foodiedash.interfaces.rest.review;

import com.ute.foodiedash.application.reviews.command.CreateReviewCommand;
import com.ute.foodiedash.application.reviews.command.UpdateReviewCommand;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.application.reviews.usecase.CreateReviewUseCase;
import com.ute.foodiedash.application.reviews.usecase.UpdateReviewUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.review.dto.CreateReviewRequestDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.ReviewResponseDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.UpdateReviewRequestDTO;
import com.ute.foodiedash.interfaces.rest.review.mapper.ReviewDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewUseCase createReviewUseCase;
    private final UpdateReviewUseCase updateReviewUseCase;
    private final ReviewDtoMapper reviewDtoMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewResponseDTO> create(
            @Valid @ModelAttribute("dto") CreateReviewRequestDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Long customerId = SecurityContextHelper.getCurrentUserId();

        CreateReviewCommand command = reviewDtoMapper.toCreateCommand(dto);
        CreateReviewCommand enriched = new CreateReviewCommand(
                command.orderId(),
                customerId,
                command.rating(),
                command.comment()
        );

        List<MultipartFile> imageFiles = images != null ? images : Collections.emptyList();
        ReviewQueryResult result = createReviewUseCase.execute(enriched, imageFiles);

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDtoMapper.toResponseDto(result));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> update(
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequestDTO dto
    ) {
        Long customerId = SecurityContextHelper.getCurrentUserId();

        UpdateReviewCommand command = reviewDtoMapper.toUpdateCommand(dto, reviewId, customerId);
        ReviewQueryResult result = updateReviewUseCase.execute(command);

        return ResponseEntity.ok(reviewDtoMapper.toResponseDto(result));
    }
}
