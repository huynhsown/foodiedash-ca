package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.command.CheckoutOrderCommand;
import com.ute.foodiedash.application.order.port.KitchenAvailabilityPort;
import com.ute.foodiedash.application.order.port.RouteCalculationPort;
import com.ute.foodiedash.application.order.query.Coordinate;
import com.ute.foodiedash.application.order.query.RouteQueryResult;
import com.ute.foodiedash.application.promotion.service.PromotionCheckoutService;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CheckoutOrderUseCase {

    private final CartRepository cartRepository;
    private final PromotionCheckoutService promotionCheckoutService;

    private final KitchenAvailabilityPort kitchenAvailabilityPort;
    private final RouteCalculationPort routeCalculationPort;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void execute(CheckoutOrderCommand command) {
        Cart cart = cartRepository.findByUserId(command.customerId())
                .orElseThrow(() -> new BadRequestException("Cart not found"));
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(()-> new BadRequestException("Restaurant not found"));
        restaurant.ensureActive();
        if(!restaurant.hasCoordinates() || command.deliveryLat() == null || command.deliveryLng() == null) {
            throw new BadRequestException("Cannot checkout");
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        if (!cart.getRestaurantId().equals(command.restaurantId())) {
            throw new BadRequestException("Cart belongs to another restaurant");
        }

        if (!kitchenAvailabilityPort.isKitchenAvailable(command.restaurantId())) {
            throw new BadRequestException("Kitchen is unavailable");
        }

        BigDecimal subtotal = cart.getItems().stream()
                .map(ci -> ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RouteQueryResult route = routeCalculationPort.calculateRoute(
                new Coordinate(restaurant.getLat(), restaurant.getLng()),
                new Coordinate(command.deliveryLat(), command.deliveryLng())
        );

        BigDecimal distanceInKm = BigDecimal.valueOf(route.distance());
        Integer etaInMinutes = 20 + route.eta();

        String lockId = null;
        BigDecimal discount = BigDecimal.ZERO;
        String orderCode = UUID.randomUUID().toString();

        if (command.voucherCode() != null && !command.voucherCode().isBlank()) {
            var promotionCheckoutData = promotionCheckoutService.prepareForCheckout(
                command.voucherCode(),
                command.customerId(),
                command.restaurantId(),
                subtotal
            );
            discount = promotionCheckoutData.discount();
        }
    }

}
