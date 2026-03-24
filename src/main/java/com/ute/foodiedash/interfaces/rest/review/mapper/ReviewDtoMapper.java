package com.ute.foodiedash.interfaces.rest.review.mapper;

import com.ute.foodiedash.application.reviews.command.CreateReviewCommand;
import com.ute.foodiedash.application.reviews.command.UpdateReviewCommand;
import com.ute.foodiedash.application.reviews.query.RestaurantReviewsQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewCustomerQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewOrderItemQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewOrderQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.interfaces.rest.review.dto.CreateReviewRequestDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.CustomerInfoDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.OrderInfoDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.OrderItemDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.RestaurantReviewsResponseDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.ReviewResponseDTO;
import com.ute.foodiedash.interfaces.rest.review.dto.UpdateReviewRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewDtoMapper {

    @Mapping(target = "customerId", ignore = true)
    CreateReviewCommand toCreateCommand(CreateReviewRequestDTO dto);

    @Mapping(target = "reviewId", source = "reviewId")
    @Mapping(target = "customerId", source = "customerId")
    UpdateReviewCommand toUpdateCommand(UpdateReviewRequestDTO dto, Long reviewId, Long customerId);

    ReviewResponseDTO toResponseDto(ReviewQueryResult result);

    RestaurantReviewsResponseDTO toResponseDto(RestaurantReviewsQueryResult result);

    OrderInfoDTO toOrderInfoDto(ReviewOrderQueryResult result);

    OrderItemDTO toOrderItemDto(ReviewOrderItemQueryResult result);

    CustomerInfoDTO toCustomerInfoDto(ReviewCustomerQueryResult result);
}
