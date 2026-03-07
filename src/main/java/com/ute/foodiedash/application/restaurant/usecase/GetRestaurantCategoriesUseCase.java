package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.query.GetRestaurantCategoriesQuery;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoriesPageResult;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryQueryResult;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.repository.RestaurantCategoryJpaRepository;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantCategoryJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetRestaurantCategoriesUseCase {
    private final RestaurantCategoryJpaRepository jpaRepository;
    private final RestaurantCategoryRepository domainRepository;

    public RestaurantCategoriesPageResult execute(GetRestaurantCategoriesQuery query) {
        Sort sort = query.ascending() 
            ? Sort.by(query.sortBy()).ascending() 
            : Sort.by(query.sortBy()).descending();
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        
        Page<RestaurantCategoryJpaEntity> page =
            jpaRepository.findAll(pageable);
        
        List<RestaurantCategoryQueryResult> content = page.getContent().stream()
            .map(jpa -> new RestaurantCategoryQueryResult(
                jpa.getId(),
                jpa.getName(),
                jpa.getIconUrl(),
                jpa.getDescription()
            ))
            .collect(Collectors.toList());
        
        return new RestaurantCategoriesPageResult(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isEmpty(),
            page.getNumberOfElements(),
            page.hasNext(),
            page.hasPrevious()
        );
    }
}
