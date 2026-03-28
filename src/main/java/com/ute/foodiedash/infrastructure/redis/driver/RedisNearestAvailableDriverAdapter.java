package com.ute.foodiedash.infrastructure.redis.driver;

import com.ute.foodiedash.application.driver.port.DriverBusyStatePort;
import com.ute.foodiedash.application.order.port.NearestAvailableDriverPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
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
    private final DriverBusyStatePort driverBusyStatePort;

    @Value("${driver.dispatch.geo-key:foodiedash:available_drivers}")
    private String geoKey;

    @Value("${driver.dispatch.geo-candidate-limit:20}")
    private int geoCandidateLimit;

    @Override
    public Optional<Long> tryReserveNearestDriver(double longitude, double latitude, double radiusKm) {
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(radiusKm, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .sortAscending()
                .limit(Math.max(1, geoCandidateLimit));

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(geoKey, circle, args);

        if (results == null || results.getContent().isEmpty()) {
            return Optional.empty();
        }

        for (GeoResult<RedisGeoCommands.GeoLocation<String>> hit : results.getContent()) {
            String member = hit.getContent().getName();
            Long driverId = parseDriverId(member);
            if (driverId == null) {
                continue;
            }
            if (driverBusyStatePort.isOnDelivery(driverId)) {
                continue;
            }
            Long removed = redisTemplate.opsForZSet().remove(geoKey, member);
            if (removed != null && removed > 0) {
                return Optional.of(driverId);
            }
        }
        return Optional.empty();
    }

    private static Long parseDriverId(String member) {
        try {
            return Long.parseLong(member.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
