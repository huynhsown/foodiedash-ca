package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.command.UpdateCartItemCommand;
import com.ute.foodiedash.application.cart.query.CartItemQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.application.menu.usecase.GetMenuItemByIdUseCase;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionValueRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateCartItemUseCase {
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final CartItemOptionValueRepository cartItemOptionValueRepository;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final GetCartItemUseCase getCartItemUseCase;

    @Transactional
    public CartItemQueryResult execute(Long cartItemId, UpdateCartItemCommand command) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new NotFoundException("Cart item not found with id " + cartItemId));

        MenuItemQueryResult menuItem = getMenuItemByIdUseCase.execute(cartItem.getMenuItemId());

        if (command.options() != null && !command.options().isEmpty()) {
            validateOptions(menuItem, command.options());
        }

        if (command.quantity() != null) {
            cartItem.updateQuantity(command.quantity());
        }

        cartItem.updateFromMenuItem(menuItem.name(), menuItem.imageUrl(), menuItem.price(), command.notes());
        BigDecimal unitTotal = calculateUnitTotalPrice(menuItem, command.options());
        cartItem.updateTotalPrice(unitTotal);

        CartItem saved = cartItemRepository.save(cartItem);

        if (command.options() != null && !command.options().isEmpty()) {
            replaceCartItemOptions(saved, command.options(), menuItem);
        }

        return getCartItemUseCase.execute(cartItemId);
    }

    private void validateOptions(MenuItemQueryResult menuItem, List<UpdateCartItemCommand.CartItemOptionCommand> options) {
        Map<Long, MenuItemOptionQueryResult> optionMap =
            menuItem.itemOptions().stream()
                .collect(Collectors.toMap(
                    MenuItemOptionQueryResult::id,
                    Function.identity()
                ));

        Set<Long> requestOptionIds = new HashSet<>();

        for (UpdateCartItemCommand.CartItemOptionCommand optionCommand : options) {
            if (!requestOptionIds.add(optionCommand.optionId())) {
                throw new BadRequestException("Duplicate optionId: " + optionCommand.optionId());
            }

            MenuItemOptionQueryResult option =
                optionMap.get(optionCommand.optionId());
            if (option == null) {
                throw new BadRequestException("Option not found in menu item");
            }

            Set<Long> validValueIds = option.itemOptionValues().stream()
                .map(MenuItemOptionValueQueryResult::id)
                .collect(Collectors.toSet());

            Set<Long> requestValueIds = new HashSet<>();
            for (UpdateCartItemCommand.CartItemOptionValueCommand valueCommand : optionCommand.values()) {
                if (!validValueIds.contains(valueCommand.optionValueId())) {
                    throw new BadRequestException(
                        "Option value not found in option " + optionCommand.optionId());
                }
                if (!requestValueIds.add(valueCommand.optionValueId())) {
                    throw new BadRequestException(
                        "Duplicate optionValueId: " + valueCommand.optionValueId());
                }
            }

            // Use domain validation logic via static-like approach (option query result)
            int selectedCount = optionCommand.values().size();
            if (Boolean.TRUE.equals(option.required()) && selectedCount == 0) {
                throw new BadRequestException("Option is required: " + option.name());
            }
            if (option.minValue() != null && selectedCount < option.minValue()) {
                throw new BadRequestException("Option min value violated: " + option.name());
            }
            if (option.maxValue() != null && selectedCount > option.maxValue()) {
                throw new BadRequestException("Option max value violated: " + option.name());
            }
        }

        for (MenuItemOptionQueryResult option : optionMap.values()) {
            if (Boolean.TRUE.equals(option.required()) && !requestOptionIds.contains(option.id())) {
                throw new BadRequestException("Missing required option: " + option.name());
            }
        }
    }

    private BigDecimal calculateUnitTotalPrice(
        MenuItemQueryResult menuItem,
        List<UpdateCartItemCommand.CartItemOptionCommand> options
    ) {
        BigDecimal total = menuItem.price();
        
        if (options != null && !options.isEmpty()) {
            Map<Long, MenuItemOptionValueQueryResult> optionValueMap =
                menuItem.itemOptions().stream()
                    .flatMap(o -> o.itemOptionValues().stream())
                    .collect(Collectors.toMap(
                        MenuItemOptionValueQueryResult::id,
                        Function.identity()
                    ));

            for (UpdateCartItemCommand.CartItemOptionCommand option : options) {
                for (UpdateCartItemCommand.CartItemOptionValueCommand value : option.values()) {
                    MenuItemOptionValueQueryResult optionValue =
                        optionValueMap.get(value.optionValueId());

                    if (optionValue != null && optionValue.extraPrice() != null) {
                        total = total.add(
                            optionValue.extraPrice()
                                .multiply(BigDecimal.valueOf(value.quantity()))
                        );
                    }
                }
            }
        }

        return total;
    }

    private void replaceCartItemOptions(
        CartItem cartItem,
        List<UpdateCartItemCommand.CartItemOptionCommand> optionCommands,
        MenuItemQueryResult menuItem
    ) {
        // Delete existing options
        List<CartItemOption> existingOptions = cartItemOptionRepository
            .findByCartItemIdAndDeletedAtIsNull(cartItem.getId());
        
        List<Long> optionIds = existingOptions.stream()
            .map(CartItemOption::getId)
            .toList();

        if (!optionIds.isEmpty()) {
            List<CartItemOptionValue> values = cartItemOptionValueRepository
                .findByCartItemOptionIdInAndDeletedAtIsNull(optionIds);
            for (CartItemOptionValue value : values) {
                cartItemOptionValueRepository.softDeleteById(value.getId());
            }
            for (CartItemOption option : existingOptions) {
                cartItemOptionRepository.softDeleteById(option.getId());
            }
        }

        Map<Long, MenuItemOptionQueryResult> optionMap =
            menuItem.itemOptions().stream()
                .collect(Collectors.toMap(
                    MenuItemOptionQueryResult::id,
                    Function.identity()
                ));

        Map<Long, MenuItemOptionValueQueryResult> valueMap =
            optionMap.values().stream()
                .flatMap(v -> v.itemOptionValues().stream())
                .collect(Collectors.toMap(
                    MenuItemOptionValueQueryResult::id,
                    Function.identity(),
                    (a, b) -> a
                ));

        for (UpdateCartItemCommand.CartItemOptionCommand optionCommand : optionCommands) {
            MenuItemOptionQueryResult itemOption =
                optionMap.get(optionCommand.optionId());
            
            if (itemOption == null) {
                continue;
            }

            CartItemOption option = new CartItemOption();
            option.setCartItemId(cartItem.getId());
            option.setOptionId(itemOption.id());
            option.setOptionName(itemOption.name());
            option.setRequired(itemOption.required());
            option.setMinValue(itemOption.minValue());
            option.setMaxValue(itemOption.maxValue());
            option = cartItemOptionRepository.save(option);

            for (UpdateCartItemCommand.CartItemOptionValueCommand valueCommand : optionCommand.values()) {
                MenuItemOptionValueQueryResult optionValue =
                    valueMap.get(valueCommand.optionValueId());
                
                if (optionValue == null) {
                    continue;
                }

                CartItemOptionValue value = new CartItemOptionValue();
                value.setCartItemOptionId(option.getId());
                value.setOptionValueId(optionValue.id());
                value.setOptionValueName(optionValue.name());
                value.setQuantity(valueCommand.quantity());
                value.setExtraPrice(optionValue.extraPrice());
                cartItemOptionValueRepository.save(value);
            }
        }
    }
}
