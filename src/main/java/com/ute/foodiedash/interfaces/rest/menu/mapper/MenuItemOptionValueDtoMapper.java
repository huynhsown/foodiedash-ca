package com.ute.foodiedash.interfaces.rest.menu.mapper;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionValueCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuItemOptionValueDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemOptionValueResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemOptionValueDtoMapper {
    CreateMenuItemOptionValueCommand toCommand(CreateMenuItemOptionValueDTO dto);
    MenuItemOptionValueResponseDTO toResponseDto(MenuItemOptionValueQueryResult result);
}
