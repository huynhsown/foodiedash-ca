package com.ute.foodiedash.infrastructure.redis.driver;

import com.ute.foodiedash.application.order.port.DriverAssignmentLockPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisDriverAssignmentLockAdapter implements DriverAssignmentLockPort {

    private final StringRedisTemplate redisTemplate;

    @Value("${driver.dispatch.lock-key-prefix:foodiedash:driver_assign_lock:}")
    private String lockKeyPrefix;

    @Override
    public boolean tryAcquire(Long orderId, Duration ttl) {
        if (orderId == null) {
            return false;
        }
        Boolean ok = redisTemplate.opsForValue()
                .setIfAbsent(lockKeyPrefix + orderId, "1", ttl);
        return Boolean.TRUE.equals(ok);
    }

    @Override
    public void release(Long orderId) {
        if (orderId == null) {
            return;
        }
        redisTemplate.delete(lockKeyPrefix + orderId);
    }
}
