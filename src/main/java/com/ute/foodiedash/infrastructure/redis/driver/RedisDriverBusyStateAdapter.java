package com.ute.foodiedash.infrastructure.redis.driver;

import com.ute.foodiedash.application.driver.port.DriverBusyStatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisDriverBusyStateAdapter implements DriverBusyStatePort {

    private final StringRedisTemplate redisTemplate;

    @Value("${driver.dispatch.driver-busy-key-prefix:foodiedash:driver_busy:}")
    private String busyKeyPrefix;

    @Override
    public void markOnDelivery(Long driverId, Long orderId, Duration ttl) {
        if (driverId == null || orderId == null) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(busyKeyPrefix + driverId, String.valueOf(orderId), ttl);
        } catch (Exception e) {
            log.error("Redis driver_busy SET failed: driverId={}, orderId={}", driverId, orderId, e);
            throw e;
        }
    }

    @Override
    public void clear(Long driverId) {
        if (driverId == null) {
            return;
        }
        try {
            redisTemplate.delete(busyKeyPrefix + driverId);
        } catch (Exception e) {
            log.warn("Redis driver_busy DEL failed: driverId={}", driverId, e);
        }
    }

    @Override
    public boolean isOnDelivery(Long driverId) {
        if (driverId == null) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(busyKeyPrefix + driverId));
        } catch (Exception e) {
            log.error("Redis driver_busy EXISTS failed: driverId={}", driverId, e);
            return false;
        }
    }
}
