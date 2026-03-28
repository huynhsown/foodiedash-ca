package com.ute.foodiedash.infrastructure.redis.driver;

import com.ute.foodiedash.application.driver.port.DriverLocationSyncPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RedisDriverLocationSyncAdapter implements DriverLocationSyncPort {

    private final StringRedisTemplate redisTemplate;

    @Value("${driver.dispatch.geo-key:foodiedash:available_drivers}")
    private String geoKey;

    @Override
    public void syncAvailableDriverLocation(Long driverId, double longitude, double latitude, Instant at) {
        redisTemplate.opsForGeo().add(geoKey, new Point(longitude, latitude), String.valueOf(driverId));
    }
}
