package com.ute.foodiedash.application.common.cache;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CacheKey {
    private static final int GEO_BUCKET_SCALE = 2;

    private CacheKey() {}

    public static String restaurantMenus(Long restaurantId) {
        return "restaurant:" + restaurantId + ":menus";
    }

    /**
     * @param lat/lng optional; when both null, key uses {@code none:none} for bucket (no geo).
     */
    public static String restaurantDetailBySlug(String slug, BigDecimal lat, BigDecimal lng) {
        return "restaurant:slug:" + slug + ":detail:" + latLngBucket(lat) + ":" + latLngBucket(lng);
    }

    public static String restaurantSnapshotById(Long id, BigDecimal lat, BigDecimal lng) {
        return "restaurant:" + id + ":snapshot:" + latLngBucket(lat) + ":" + latLngBucket(lng);
    }

    public static String restaurantCategoriesAll() {
        return "restaurant:categories:all";
    }

    public static String restaurantCategoriesPage(int page, int size, String sortBy, boolean ascending) {
        return "restaurant:categories:page:" + page + ":size:" + size + ":sort:" + sortBy + ":asc:" + ascending;
    }

    public static String menuItem(Long menuItemId) {
        return "menu:item:" + menuItemId;
    }

    public static String menuItemOptions(Long menuItemId) {
        return "menu:item:" + menuItemId + ":options";
    }

    private static String latLngBucket(BigDecimal value) {
        if (value == null) {
            return "none";
        }
        return value.setScale(GEO_BUCKET_SCALE, RoundingMode.HALF_UP).toPlainString();
    }
}
