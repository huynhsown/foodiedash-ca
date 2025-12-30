package com.ute.foodiedash.interfaces.ws.driver.dto;

public record DriverGpsHeartbeatMessage(
        Double lat,
        Double lng,
        Long ts
) {}