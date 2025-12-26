package com.ute.foodiedash.interfaces.rest.user.mapper;

import com.ute.foodiedash.application.user.command.CreateCustomerAddressCommand;
import com.ute.foodiedash.application.user.command.UpdateCustomerAddressCommand;
import com.ute.foodiedash.application.user.query.CustomerAddressQueryResult;
import com.ute.foodiedash.application.user.query.CustomerAddressesQueryResult;
import com.ute.foodiedash.interfaces.rest.user.dto.CreateCustomerAddressDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.CustomerAddressResponseDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.CustomerAddressesResponseDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.UpdateCustomerAddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerAddressDtoMapper {

    CreateCustomerAddressCommand toCreateCommand(CreateCustomerAddressDTO dto);

    @Mapping(source = "addressId", target = "addressId")
    UpdateCustomerAddressCommand toUpdateCommand(Long addressId, UpdateCustomerAddressDTO dto);

    @Mapping(source = "isDefault", target = "defaultAddress")
    CustomerAddressResponseDTO toResponseDto(CustomerAddressQueryResult result);

    CustomerAddressesResponseDTO toResponseDto(CustomerAddressesQueryResult result);
}

