package com.ute.foodiedash.domain.restaurant.service;

import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugGenerator {
    private static final Pattern DIACRITICS_PATTERN =
            Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private final RestaurantRepository restaurantRepository;

    public SlugGenerator(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public String generateUniqueSlug(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be empty");
        }

        String slug = Normalizer.normalize(name, Normalizer.Form.NFD);
        slug = DIACRITICS_PATTERN.matcher(slug).replaceAll("");

        slug = slug.replace("đ", "d").replace("Đ", "d");

        slug = slug.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-");

        String uniqueSlug = slug;
        int counter = 1;
        while (restaurantRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = slug + "-" + counter++;
        }

        return uniqueSlug;
    }
}
