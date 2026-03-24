package com.ute.foodiedash.interfaces.rest.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantReviewsResponseDTO {
    private List<ReviewResponseDTO> reviews;
    private int totalCount;
}
