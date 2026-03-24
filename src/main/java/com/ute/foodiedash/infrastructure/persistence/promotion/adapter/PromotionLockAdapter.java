package com.ute.foodiedash.infrastructure.persistence.promotion.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.application.order.port.PromotionLockPort;
import com.ute.foodiedash.application.order.query.VoucherLockResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PromotionLockAdapter implements PromotionLockPort {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${voucher.lock.ttl-seconds:900}")
    private long ttlSeconds;


    @Override
    public VoucherLockResult holdPromotion(String code, Long customerId, BigDecimal subtotal) {
        String lockId = UUID.randomUUID().toString();

        String voucherKey = voucherKey(code);
        String lockKey = lockKey(lockId);

        VoucherLockPayload payload = new VoucherLockPayload(
                lockId, code, customerId, subtotal, "HELD", null
        );

        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(voucherKey, lockId, Duration.ofSeconds(ttlSeconds));

        if (acquired == null || !acquired) {
            return new VoucherLockResult(null, false, "ALREADY_LOCKED");
        }

        redisTemplate.opsForValue().set(
                lockKey,
                write(payload),
                Duration.ofSeconds(ttlSeconds)
        );

        return new VoucherLockResult(lockId, true, "OK");
    }

    private String voucherKey(String code) {
        return "voucher:lock:code:" + code;
    }

    private String lockKey(String lockId) {
        return "voucher:lock:id:" + lockId;
    }

    private String write(VoucherLockPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Cannot serialize voucher lock payload");
        }
    }

    private VoucherLockPayload read(String raw) {
        try {
            return objectMapper.readValue(raw, VoucherLockPayload.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Cannot deserialize voucher lock payload");
        }
    }

    public record VoucherLockPayload(
            String lockId,
            String voucherCode,
            Long customerId,
            BigDecimal subtotal,
            String status, // HELD | CONFIRMED
            Long orderId
    ) {}
}
