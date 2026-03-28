package com.ute.foodiedash.infrastructure.redis.driver;

import com.ute.foodiedash.application.order.port.NearestAvailableDriverPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisNearestAvailableDriverAdapter implements NearestAvailableDriverPort {

    private final StringRedisTemplate redisTemplate;

    @Value("${driver.dispatch.geo-key:foodiedash:available_drivers}")
    private String geoKey;

    @Override
    public Optional<Long> tryReserveNearestDriver(double longitude, double latitude, double radiusKm) {
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(radiusKm, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .sortAscending()
                .limit(1);

        GeoResults<org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(geoKey, circle, args);

        if (results == null || results.getContent().isEmpty()) {
            return Optional.empty();
        }

        String member = results.getContent().get(0).getContent().getName();
        Long removed = redisTemplate.opsForZSet().remove(geoKey, member);
        if (removed == null || removed == 0) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.parseLong(member.trim()));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
