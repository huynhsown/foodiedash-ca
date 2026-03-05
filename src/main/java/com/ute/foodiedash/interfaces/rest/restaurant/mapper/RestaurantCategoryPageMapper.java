package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.query.GetRestaurantCategoriesQuery;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoriesPageResult;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryQueryResult;
import com.ute.foodiedash.interfaces.rest.common.dto.PageInfo;
import com.ute.foodiedash.interfaces.rest.common.dto.PageRequestDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantCategoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RestaurantCategoryDtoMapper.class)
public interface RestaurantCategoryPageMapper {
    GetRestaurantCategoriesQuery toQuery(PageRequestDTO dto);
    
    @Mapping(target = "pageContent", source = "content")
    @Mapping(target = "hasNextPage", source = "hasNextPage")
    @Mapping(target = "hasPreviousPage", source = "hasPreviousPage")
    PageInfo<RestaurantCategoryResponseDTO> toPageInfo(RestaurantCategoriesPageResult result);
}
