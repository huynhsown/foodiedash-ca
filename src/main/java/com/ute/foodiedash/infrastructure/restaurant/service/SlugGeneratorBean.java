package com.ute.foodiedash.infrastructure.restaurant.service;

import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.domain.restaurant.service.SlugGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlugGeneratorBean {
    @Bean
    public SlugGenerator slugGenerator(RestaurantRepository restaurantRepository) {
        return new SlugGenerator(restaurantRepository);
    }
}
