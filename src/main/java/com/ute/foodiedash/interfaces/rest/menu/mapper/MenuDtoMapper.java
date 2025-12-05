package com.ute.foodiedash.interfaces.rest.menu.mapper;

import com.ute.foodiedash.application.menu.command.CreateMenuCommand;
import com.ute.foodiedash.application.menu.query.MenuQueryResult;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuDtoMapper {
    CreateMenuCommand toCommand(CreateMenuDTO dto);
    MenuResponseDTO toResponseDto(MenuQueryResult result);
}
