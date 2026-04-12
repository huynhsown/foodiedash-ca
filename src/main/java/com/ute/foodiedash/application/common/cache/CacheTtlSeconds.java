package com.ute.foodiedash.application.common.cache;

/**
 * TTL (seconds) for cache-aside reads. Tune per environment if needed.
 */
public final class CacheTtlSeconds {
    private CacheTtlSeconds() {}

    /** Menu aggregate: target range 30–120s */
    public static final long RESTAURANT_MENU = 60L;

    /** Detail by slug: target range 60–300s */
    public static final long RESTAURANT_DETAIL_BY_SLUG = 180L;

    /** Snapshot by id: target range 60–300s */
    public static final long RESTAURANT_SNAPSHOT_BY_ID = 180L;

    /** Category lists: target range 10–60 minutes */
    public static final long RESTAURANT_CATEGORIES = 600L;

    /** Menu item / options: target range 1–5 minutes */
    public static final long MENU_ITEM = 120L;
    public static final long MENU_ITEM_OPTIONS = 120L;
}
