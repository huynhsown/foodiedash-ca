package com.ute.foodiedash.infrastructure.websocket;

import java.security.Principal;

public record DriverWebSocketPrincipal(Long userId) implements Principal {

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
