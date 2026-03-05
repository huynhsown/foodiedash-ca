package com.ute.foodiedash.infrastructure.util;

import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantUtils {

    private final RestaurantBusinessHourRepository restaurantBusinessHourRepository;

    public boolean checkIfRestaurantIsOpen(Long restaurantId) {
        try {
            List<RestaurantBusinessHour> businessHours = restaurantBusinessHourRepository
                    .findByRestaurantId(restaurantId, false);

            if (businessHours.isEmpty()) {
                return false;
            }

            DayOfWeek currentDay = DayOfWeek.from(java.time.LocalDate.now());
            LocalTime currentTime = LocalTime.now();

            return businessHours.stream()
                    .anyMatch(bh -> bh.isOpenAt(currentDay, currentTime));
        } catch (Exception e) {
            log.error("Error checking if restaurant {} is open", restaurantId, e);
            return false;
        }
    }

    public static Double calculateDistanceKm(BigDecimal userLat, BigDecimal userLng, BigDecimal restaurantLat, BigDecimal restaurantLng) {
        if (userLat == null || userLng == null || restaurantLat == null || restaurantLng == null) {
            return null;
        }

        double lat1 = userLat.doubleValue();
        double lng1 = userLng.doubleValue();
        double lat2 = restaurantLat.doubleValue();
        double lng2 = restaurantLng.doubleValue();

        double earthRadiusKm = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadiusKm * c;

        return Math.round(distance * 100.0) / 100.0;
    }

    public static Double calculateDistanceKm(BigDecimal userLat, BigDecimal userLng, Double restaurantLat, Double restaurantLng) {
        if (userLat == null || userLng == null || restaurantLat == null || restaurantLng == null) {
            return null;
        }

        double lat1 = userLat.doubleValue();
        double lng1 = userLng.doubleValue();
        double lat2 = restaurantLat;
        double lng2 = restaurantLng;

        double earthRadiusKm = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadiusKm * c;

        return Math.round(distance * 100.0) / 100.0;
    }

    public static Double calculateDistanceKm(BigDecimal userLat, BigDecimal userLng, RestaurantSearchDocument doc) {
        if (userLat == null || userLng == null || doc == null || doc.getGeo() == null) {
            return null;
        }

        return calculateDistanceKm(userLat, userLng, doc.getGeo().getLat(), doc.getGeo().getLng());
    }

    public static Integer calculateEtaMinutes(Double distanceKm, Integer prepTimeAvg) {
        if (distanceKm == null && prepTimeAvg == null) {
            return null;
        }

        double travelMinutes = 0.0;
        if (distanceKm != null) {
            double speedKmPerHour = 30.0;
            travelMinutes = (distanceKm / speedKmPerHour) * 60.0;
        }

        double prep = prepTimeAvg != null ? prepTimeAvg : 0.0;
        return (int) Math.round(travelMinutes + prep);
    }
}
