package com.ute.foodiedash.infrastructure.redis.driver;

import com.ute.foodiedash.application.order.port.DriverAssignmentPendingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisDriverAssignmentPendingAdapter implements DriverAssignmentPendingPort {

    private final StringRedisTemplate redisTemplate;

    @Value("${driver.dispatch.pending-set-key:foodiedash:driver_assignment_pending}")
    private String pendingSetKey;

    @Override
    public void enqueue(Long orderId) {
        if (orderId == null) {
            return;
        }
        redisTemplate.opsForSet().add(pendingSetKey, String.valueOf(orderId));
    }

    @Override
    public void remove(Long orderId) {
        if (orderId == null) {
            return;
        }
        redisTemplate.opsForSet().remove(pendingSetKey, String.valueOf(orderId));
    }

    @Override
    public Set<Long> snapshotPendingOrderIds() {
        Set<String> members = redisTemplate.opsForSet().members(pendingSetKey);
        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }
        List<Long> ids = members.stream()
                .map(s -> {
                    try {
                        return Long.parseLong(s.trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(id -> id != null)
                .collect(Collectors.toList());
        return Set.copyOf(ids);
    }
}
