package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCategoryMapCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryMapQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantCategoryMapDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantCategoryMapResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantCategoryMapDtoMapper {
    CreateRestaurantCategoryMapCommand toCommand(CreateRestaurantCategoryMapDTO dto);
    RestaurantCategoryMapResponseDTO toResponseDto(RestaurantCategoryMapQueryResult result);
}
