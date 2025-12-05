package com.ute.foodiedash.interfaces.rest.menu.mapper;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuItemOptionDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemOptionResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MenuItemOptionValueDtoMapper.class)
public interface MenuItemOptionDtoMapper {
    CreateMenuItemOptionCommand toCommand(CreateMenuItemOptionDTO dto);
    MenuItemOptionResponseDTO toResponseDto(MenuItemOptionQueryResult result);
}
