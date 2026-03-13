package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.command.AddToCartCommand;
import com.ute.foodiedash.application.cart.query.CartQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.application.menu.usecase.GetMenuItemByIdUseCase;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddToCartUseCaseV2 {

    private final CartRepository cartRepository;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final MenuRepository menuRepository;
    private final GetCartUseCase getCartUseCase;

    @Transactional
    public CartQueryResult execute(Long userId, AddToCartCommand command) {
        Cart cart = findOrCreateActiveCart(userId, command.restaurantId());

        cart.extendExpiry();

        MenuItemQueryResult menuItem = getMenuItemByIdUseCase.execute(command.menuItemId());

        validateMenuItemForCart(command, menuItem);

        Optional<CartItem> existingItem = findMatchingCartItem(cart, command);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.updateQuantity(item.getQuantity() + command.quantity());
            item.updateFromMenuItem(menuItem.name(), menuItem.imageUrl(), menuItem.price(), command.notes());
        } else {
            CartItem newItem = createCartItemFromMenu(menuItem, command);

            if (command.options() != null && !command.options().isEmpty()) {
                applyOptionsFromCommand(newItem, menuItem, command);
            }

            cart.addItem(newItem);
        }

        cart = cartRepository.save(cart);

        return getCartUseCase.execute(cart.getId());
    }

    private Cart findOrCreateActiveCart(Long userId, Long restaurantId) {
        return cartRepository
                .findActiveCart(userId, restaurantId)
                .orElseGet(() -> {
                    removeAllActiveCarts(userId);
                    Cart cart = Cart.createForUser(userId, restaurantId);
                    return cartRepository.save(cart);
                });
    }

    private void validateMenuItemForCart(AddToCartCommand command, MenuItemQueryResult menuItem) {
        if (!command.restaurantId().equals(menuItem.restaurantId())) {
            throw new BadRequestException("Menu item does not belong to restaurant");
        }

        if (menuItem.status() != MenuItemStatus.ACTIVE) {
            throw new BadRequestException("Menu item is not active");
        }

        Menu menu = menuRepository.findById(menuItem.menuId())
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        // dùng guard method trong domain Menu
        menu.ensureActive();
    }

    private CartItem createCartItemFromMenu(MenuItemQueryResult menuItem, AddToCartCommand command) {
        return CartItem.create(
                menuItem.id(),
                menuItem.name(),
                menuItem.imageUrl(),
                command.quantity(),
                menuItem.price(),
                command.notes()
        );
    }

    private void applyOptionsFromCommand(
            CartItem cartItem,
            MenuItemQueryResult menuItem,
            AddToCartCommand command
    ) {
        Map<Long, MenuItemOptionQueryResult> optionMap =
                menuItem.itemOptions().stream()
                        .collect(Collectors.toMap(
                                MenuItemOptionQueryResult::id,
                                Function.identity()
                        ));

        Map<Long, MenuItemOptionValueQueryResult> valueMap =
                optionMap.values().stream()
                        .flatMap(o -> o.itemOptionValues().stream())
                        .collect(Collectors.toMap(
                                MenuItemOptionValueQueryResult::id,
                                Function.identity(),
                                (a, b) -> a
                        ));

        for (AddToCartCommand.CartItemOptionCommand optionCommand : command.options()) {
            MenuItemOptionQueryResult optionDef = optionMap.get(optionCommand.optionId());
            if (optionDef == null) {
                throw new BadRequestException("Option not found in menu item");
            }

            CartItemOption option = CartItemOption.create(
                    optionDef.id(),
                    optionDef.name(),
                    optionDef.required(),
                    optionDef.minValue(),
                    optionDef.maxValue()
            );

            for (AddToCartCommand.CartItemOptionValueCommand valueCommand : optionCommand.values()) {
                MenuItemOptionValueQueryResult valueDef = valueMap.get(valueCommand.optionValueId());
                if (valueDef == null) {
                    throw new BadRequestException(
                            "Option value not found in option " + optionCommand.optionId());
                }

                CartItemOptionValue value = CartItemOptionValue.create(
                        valueDef.id(),
                        valueDef.name(),
                        valueCommand.quantity(),
                        valueDef.extraPrice()
                );

                option.addValue(value);
            }

            option.validateSelection();

            cartItem.addOption(option);
        }
    }

    private Optional<CartItem> findMatchingCartItem(Cart cart, AddToCartCommand command) {
        String requestSignature = buildOptionSignature(command.options());
        return cart.getItems().stream()
                .filter(item ->
                        item.getMenuItemId().equals(command.menuItemId()) &&
                                buildOptionSignatureFromEntity(item).equals(requestSignature))
                .findFirst();
    }

    private String buildOptionSignature(List<AddToCartCommand.CartItemOptionCommand> options) {
        if (options == null || options.isEmpty()) {
            return "";
        }

        return options.stream()
                .sorted(Comparator.comparing(AddToCartCommand.CartItemOptionCommand::optionId))
                .map(option ->
                        option.optionId() + ":" +
                                option.values().stream()
                                        .sorted(Comparator.comparing(AddToCartCommand.CartItemOptionValueCommand::optionValueId))
                                        .map(v -> v.optionValueId() + "x" + v.quantity())
                                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining("|"));
    }

    private String buildOptionSignatureFromEntity(CartItem cartItem) {
        if (cartItem.getOptions() == null || cartItem.getOptions().isEmpty()) {
            return "";
        }

        return cartItem.getOptions().stream()
                .sorted(Comparator.comparing(CartItemOption::getOptionId))
                .map(option -> {
                    String valuePart = option.getValues().stream()
                            .sorted(Comparator.comparing(CartItemOptionValue::getOptionValueId))
                            .map(v -> v.getOptionValueId() + "x" + v.getQuantity())
                            .collect(Collectors.joining(","));
                    return option.getOptionId() + ":" + valuePart;
                })
                .collect(Collectors.joining("|"));
    }

    private void removeAllActiveCarts(Long userId) {
        List<Cart> carts = cartRepository
                .findByUserIdAndStatusAndDeletedAtIsNull(userId, CartStatus.ACTIVE);
        if (!carts.isEmpty()) {
            cartRepository.deleteAll(carts);
        }
    }
}

