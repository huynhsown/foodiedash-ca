package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.command.AddToCartCommand;
import com.ute.foodiedash.application.cart.query.CartQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.application.menu.usecase.GetMenuItemByIdUseCase;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionValueRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddToCartUseCase {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final CartItemOptionValueRepository cartItemOptionValueRepository;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final MenuRepository menuRepository;
    private final GetCartUseCase getCartUseCase;

    @Transactional
    public CartQueryResult execute(Long userId, AddToCartCommand command) {
        Cart cart = cartRepository
            .findActiveCart(userId, command.restaurantId())
            .orElseGet(() -> {
                removeAllCarts(userId);
                return createCart(userId, command.restaurantId());
            });

        cart.extendExpiry();
        cart = cartRepository.save(cart);

        MenuItemQueryResult menuItem = getMenuItemByIdUseCase.execute(command.menuItemId());

        validateAddToCart(menuItem, command);

        Optional<CartItem> existingItem = findMatchingCartItem(cart, command);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.updateQuantity(item.getQuantity() + command.quantity());
            item.updateFromMenuItem(menuItem.name(), menuItem.imageUrl(), menuItem.price(), command.notes());
            BigDecimal unitTotal = calculateUnitTotalPrice(menuItem, command);
            item.updateTotalPrice(unitTotal);
            
            cartItemRepository.save(item);
        } else {
            CartItem item = createCartItem(cart, menuItem, command);
            createCartItemOptions(item, command, menuItem);
        }

        return getCartUseCase.execute(cart.getId());
    }

    private void validateAddToCart(MenuItemQueryResult menuItem, AddToCartCommand command) {
        if (!command.restaurantId().equals(menuItem.restaurantId())) {
            throw new BadRequestException("Menu item does not belong to restaurant");
        }

        if (menuItem.status() != MenuItemStatus.ACTIVE) {
            throw new BadRequestException("Menu item is not active");
        }

        Menu menu = menuRepository.findById(menuItem.menuId())
            .orElseThrow(() -> new NotFoundException("Menu not found"));
        
        menu.ensureActive();

        if (command.options() != null && !command.options().isEmpty()) {
            validateOptions(menuItem, command.options());
        }
    }

    private void validateOptions(MenuItemQueryResult menuItem, List<AddToCartCommand.CartItemOptionCommand> options) {
        Map<Long, MenuItemOptionQueryResult> optionMap =
            menuItem.itemOptions().stream()
                .collect(Collectors.toMap(
                    MenuItemOptionQueryResult::id,
                    Function.identity()
                ));

        Set<Long> requestOptionIds = new HashSet<>();

        for (AddToCartCommand.CartItemOptionCommand optionCommand : options) {
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
            for (AddToCartCommand.CartItemOptionValueCommand valueCommand : optionCommand.values()) {
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

    private Cart createCart(Long userId, Long restaurantId) {
        Cart cart = Cart.createForUser(userId, restaurantId);
        return cartRepository.save(cart);
    }

    private CartItem createCartItem(Cart cart, MenuItemQueryResult menuItem, AddToCartCommand command) {
        CartItem item = new CartItem();
        item.setCartId(cart.getId());
        item.setMenuItemId(menuItem.id());
        item.setQuantity(command.quantity());
        item.setUnitPrice(menuItem.price());
        item.setName(menuItem.name());
        item.setImageUrl(menuItem.imageUrl());
        item.setNotes(command.notes());
        
        BigDecimal unitTotal = calculateUnitTotalPrice(menuItem, command);
        item.setTotalPrice(unitTotal.multiply(BigDecimal.valueOf(command.quantity())));
        
        return cartItemRepository.save(item);
    }

    private void createCartItemOptions(
        CartItem cartItem,
        AddToCartCommand command,
        MenuItemQueryResult menuItem
    ) {
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

        if (command.options() != null) {
            for (AddToCartCommand.CartItemOptionCommand optionCommand : command.options()) {
                MenuItemOptionQueryResult itemOption =
                    optionMap.get(optionCommand.optionId());

                CartItemOption option = new CartItemOption();
                option.setCartItemId(cartItem.getId());
                option.setOptionId(itemOption.id());
                option.setOptionName(itemOption.name());
                option.setRequired(itemOption.required());
                option.setMinValue(itemOption.minValue());
                option.setMaxValue(itemOption.maxValue());
                option = cartItemOptionRepository.save(option);

                for (AddToCartCommand.CartItemOptionValueCommand valueCommand : optionCommand.values()) {
                    MenuItemOptionValueQueryResult optionValue =
                        valueMap.get(valueCommand.optionValueId());
                    
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

    private Optional<CartItem> findMatchingCartItem(Cart cart, AddToCartCommand command) {
        String requestSignature = buildOptionSignature(command.options());
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        
        return cartItems.stream()
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
                    .collect(Collectors.joining(","))
            )
            .collect(Collectors.joining("|"));
    }

    private String buildOptionSignatureFromEntity(CartItem cartItem) {
        List<CartItemOption> cartItemOptions = cartItemOptionRepository
            .findByCartItemIdAndDeletedAtIsNull(cartItem.getId());
        List<Long> optionIds = cartItemOptions.stream()
            .map(CartItemOption::getId)
            .toList();
        List<CartItemOptionValue> values = cartItemOptionValueRepository
            .findByCartItemOptionIds(optionIds);

        Map<Long, List<CartItemOptionValue>> valueMap = values.stream()
            .collect(Collectors.groupingBy(CartItemOptionValue::getCartItemOptionId));

        return cartItemOptions.stream()
            .sorted(Comparator.comparing(CartItemOption::getOptionId))
            .map(option -> {
                String valuePart = valueMap.getOrDefault(option.getId(), List.of())
                    .stream()
                    .sorted(Comparator.comparing(CartItemOptionValue::getOptionValueId))
                    .map(v -> v.getOptionValueId() + "x" + v.getQuantity())
                    .collect(Collectors.joining(","));
                return option.getOptionId() + ":" + valuePart;
            })
            .collect(Collectors.joining("|"));
    }

    private BigDecimal calculateUnitTotalPrice(
        MenuItemQueryResult menuItem,
        AddToCartCommand command
    ) {
        BigDecimal total = menuItem.price();
        
        if (command.options() != null && !command.options().isEmpty()) {
            Map<Long, MenuItemOptionValueQueryResult> optionValueMap =
                menuItem.itemOptions().stream()
                    .flatMap(o -> o.itemOptionValues().stream())
                    .collect(Collectors.toMap(
                        MenuItemOptionValueQueryResult::id,
                        Function.identity()
                    ));

            for (AddToCartCommand.CartItemOptionCommand option : command.options()) {
                for (AddToCartCommand.CartItemOptionValueCommand value : option.values()) {
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

    private void removeAllCarts(Long userId) {
        List<Cart> carts = cartRepository
            .findByUserIdAndStatusAndDeletedAtIsNull(userId, CartStatus.ACTIVE);
        if (!carts.isEmpty()) {
            cartRepository.deleteAll(carts);
        }
    }
}
