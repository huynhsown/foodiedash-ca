package com.ute.foodiedash.application.common.port;

import com.fasterxml.jackson.core.type.TypeReference;

public interface CachePort {
    <T> void set(String key, T value, long ttlSeconds);

    <T> T get(String key, Class<T> tClass);

    <T> T get(String key, TypeReference<T> typeReference);

    void delete(String key);
}
