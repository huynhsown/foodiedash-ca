package com.ute.foodiedash.infrastructure.persistence.kitchen.adapter;

import com.ute.foodiedash.application.order.port.KitchenAvailabilityPort;
import org.springframework.stereotype.Component;

@Component
public class KitchenAdapter implements KitchenAvailabilityPort {
    @Override
    public boolean isKitchenAvailable(Long restaurantId) {
        return true;
    }
}
