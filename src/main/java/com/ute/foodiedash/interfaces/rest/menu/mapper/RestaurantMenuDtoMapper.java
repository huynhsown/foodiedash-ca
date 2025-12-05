package com.ute.foodiedash.interfaces.rest.menu.mapper;

import com.ute.foodiedash.application.menu.query.MenuDetailQueryResult;
import com.ute.foodiedash.application.menu.query.RestaurantMenuQueryResult;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuDetailDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.RestaurantMenuResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MenuItemDtoMapper.class)
public interface RestaurantMenuDtoMapper {
    @Mapping(target = "isAvailable", source = "isAvailable")
    @Mapping(target = "menuItems", source = "menuItems", qualifiedByName = "toMenuItemResponseDto")
    MenuDetailDTO toMenuDetailDto(MenuDetailQueryResult result);
    
    RestaurantMenuResponseDTO toResponseDto(RestaurantMenuQueryResult result);
}
