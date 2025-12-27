package com.ute.foodiedash.interfaces.rest.user.mapper;

import com.ute.foodiedash.application.user.command.UpdateCustomerProfileCommand;
import com.ute.foodiedash.application.user.query.CustomerProfileQueryResult;
import com.ute.foodiedash.interfaces.rest.user.dto.CustomerProfileResponseDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.UpdateCustomerProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerProfileDtoMapper {

    UpdateCustomerProfileCommand toCommand(UpdateCustomerProfileDTO dto);

    CustomerProfileResponseDTO toResponseDto(CustomerProfileQueryResult result);
}

