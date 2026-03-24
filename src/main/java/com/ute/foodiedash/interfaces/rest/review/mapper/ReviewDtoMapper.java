package com.ute.foodiedash.interfaces.rest.review.mapper;

import com.ute.foodiedash.application.reviews.command.CreateReviewCommand;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.interfaces.rest.review.dto.CreateReviewRequestDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.ReviewResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewDtoMapper {

    @Mapping(target = "customerId", ignore = true)
    CreateReviewCommand toCreateCommand(CreateReviewRequestDTO dto);

    ReviewResponseDTO toResponseDto(ReviewQueryResult result);
}
