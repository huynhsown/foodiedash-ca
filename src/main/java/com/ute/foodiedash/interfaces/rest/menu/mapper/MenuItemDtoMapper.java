package com.ute.foodiedash.interfaces.rest.menu.mapper;

import com.ute.foodiedash.application.menu.command.CreateMenuItemCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuItemDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemOptionResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemOptionValueResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemDtoMapper {
    CreateMenuItemCommand toCommand(CreateMenuItemDTO dto);
    
    @Named("toMenuItemResponseDto")
    MenuItemResponseDTO toResponseDto(MenuItemQueryResult result);
    MenuItemOptionResponseDTO toOptionResponseDto(MenuItemOptionQueryResult result);
    MenuItemOptionValueResponseDTO toOptionValueResponseDto(MenuItemOptionValueQueryResult result);
    
    @Named("toMenuItemResponseDtoWithOptions")
    default MenuItemResponseDTO toResponseDtoWithOptions(MenuItemQueryResult result) {
        MenuItemResponseDTO dto = toResponseDto(result);
        if (result.itemOptions() != null) {
            List<MenuItemOptionResponseDTO> options = result.itemOptions().stream()
                .map(this::toOptionResponseDto)
                .toList();
            dto.setItemOptions(options);
        }
        return dto;
    }
}
