package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCategoryCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantCategoryDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantCategoryResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantCategoryDtoMapper {
    CreateRestaurantCategoryCommand toCommand(CreateRestaurantCategoryDTO dto);
    RestaurantCategoryResponseDTO toResponseDto(RestaurantCategoryQueryResult result);
}
