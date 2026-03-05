package com.ute.foodiedash.interfaces.rest.cart.mapper;

import com.ute.foodiedash.application.cart.command.AddToCartCommand;
import com.ute.foodiedash.application.cart.query.CartCountQueryResult;
import com.ute.foodiedash.application.cart.query.CartItemOptionQueryResult;
import com.ute.foodiedash.application.cart.query.CartItemOptionValueQueryResult;
import com.ute.foodiedash.application.cart.query.CartItemQueryResult;
import com.ute.foodiedash.application.cart.query.CartQueryResult;
import com.ute.foodiedash.interfaces.rest.cart.dto.AddToCartRequestDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartCountResponseDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartItemDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartItemOptionDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartItemOptionRequestDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartItemOptionValueDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartItemOptionValueRequestDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.UpdateCartItemRequestDTO;
import com.ute.foodiedash.application.cart.command.UpdateCartItemCommand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartDtoMapper {
    default AddToCartCommand toCommand(AddToCartRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new AddToCartCommand(
            dto.getRestaurantId(),
            dto.getMenuItemId(),
            dto.getQuantity(),
            dto.getNotes(),
            mapOptions(dto.getOptions())
        );
    }
    
    default UpdateCartItemCommand toUpdateCommand(UpdateCartItemRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UpdateCartItemCommand(
            null,
            dto.getQuantity(),
            dto.getNotes(),
            mapUpdateOptions(dto.getOptions())
        );
    }
    
    default List<UpdateCartItemCommand.CartItemOptionCommand> mapUpdateOptions(
        List<CartItemOptionRequestDTO> options) {
        if (options == null) {
            return null;
        }
        return options.stream()
            .map(option -> new UpdateCartItemCommand.CartItemOptionCommand(
                option.getOptionId(),
                mapUpdateOptionValues(option.getValues())
            ))
            .toList();
    }
    
    default List<UpdateCartItemCommand.CartItemOptionValueCommand> mapUpdateOptionValues(
        List<CartItemOptionValueRequestDTO> values) {
        if (values == null) {
            return null;
        }
        return values.stream()
            .map(value -> new UpdateCartItemCommand.CartItemOptionValueCommand(
                value.getOptionValueId(),
                value.getQuantity()
            ))
            .toList();
    }
    
    default CartDTO toDto(CartQueryResult result) {
        if (result == null) {
            return null;
        }
        CartDTO dto = new CartDTO();
        dto.setId(result.id());
        dto.setUserId(result.userId());
        dto.setRestaurantId(result.restaurantId());
        dto.setStatus(result.status());
        dto.setExpiresAt(result.expiresAt());
        dto.setTotalPrice(result.totalPrice());
        dto.setTotalItems(result.totalItems());
        dto.setItems(mapCartItems(result.items()));
        return dto;
    }
    
    default CartCountResponseDTO toCountDto(CartCountQueryResult result) {
        if (result == null) {
            return null;
        }
        return new CartCountResponseDTO(result.totalItems());
    }

    default List<AddToCartCommand.CartItemOptionCommand> mapOptions(
        List<CartItemOptionRequestDTO> options) {
        if (options == null) {
            return null;
        }
        return options.stream()
            .map(option -> new AddToCartCommand.CartItemOptionCommand(
                option.getOptionId(),
                mapOptionValues(option.getValues())
            ))
            .toList();
    }
    
    default List<AddToCartCommand.CartItemOptionValueCommand> mapOptionValues(
        List<CartItemOptionValueRequestDTO> values) {
        if (values == null) {
            return null;
        }
        return values.stream()
            .map(value -> new AddToCartCommand.CartItemOptionValueCommand(
                value.getOptionValueId(),
                value.getQuantity()
            ))
            .toList();
    }
    
    default List<CartItemDTO> mapCartItems(List<CartItemQueryResult> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
            .map(this::mapCartItem)
            .toList();
    }
    
    default CartItemDTO mapCartItem(CartItemQueryResult item) {
        if (item == null) {
            return null;
        }
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.id());
        dto.setMenuItemId(item.menuItemId());
        dto.setName(item.name());
        dto.setImageUrl(item.imageUrl());
        dto.setNotes(item.notes());
        dto.setQuantity(item.quantity());
        dto.setUnitPrice(item.unitPrice());
        dto.setTotalPrice(item.totalPrice());
        dto.setOptions(mapCartItemOptions(item.options()));
        return dto;
    }
    
    default List<CartItemOptionDTO> mapCartItemOptions(List<CartItemOptionQueryResult> options) {
        if (options == null) {
            return null;
        }
        return options.stream()
            .map(this::mapCartItemOption)
            .toList();
    }
    
    default CartItemOptionDTO mapCartItemOption(CartItemOptionQueryResult option) {
        if (option == null) {
            return null;
        }
        CartItemOptionDTO dto = new CartItemOptionDTO();
        dto.setId(option.id());
        dto.setOptionId(option.optionId());
        dto.setOptionName(option.optionName());
        dto.setRequired(option.required());
        dto.setMinValue(option.minValue());
        dto.setMaxValue(option.maxValue());
        dto.setValues(mapCartItemOptionValues(option.values()));
        return dto;
    }
    
    default List<CartItemOptionValueDTO> mapCartItemOptionValues(
        List<CartItemOptionValueQueryResult> values) {
        if (values == null) {
            return null;
        }
        return values.stream()
            .map(this::mapCartItemOptionValue)
            .toList();
    }
    
    default CartItemOptionValueDTO mapCartItemOptionValue(CartItemOptionValueQueryResult value) {
        if (value == null) {
            return null;
        }
        CartItemOptionValueDTO dto = new CartItemOptionValueDTO();
        dto.setId(value.id());
        dto.setOptionValueId(value.optionValueId());
        dto.setOptionValueName(value.optionValueName());
        dto.setQuantity(value.quantity());
        dto.setExtraPrice(value.extraPrice());
        return dto;
    }
}
