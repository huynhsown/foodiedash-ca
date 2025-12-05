package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantRatingCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantRatingQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantRatingDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantRatingResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantRatingDtoMapper {
    CreateRestaurantRatingCommand toCommand(CreateRestaurantRatingDTO dto);
    RestaurantRatingResponseDTO toResponseDto(RestaurantRatingQueryResult result);
}
