package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.MenuJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuRepositoryAdapter implements MenuRepository {
    private final MenuJpaRepository jpaRepository;
    private final MenuJpaMapper mapper;

    @Override
    public Menu save(Menu menu) {
        MenuJpaEntity jpaEntity = mapper.toJpaEntity(menu);
        MenuJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Menu> findByRestaurantId(Long restaurantId) {
        return jpaRepository.findByRestaurantId(restaurantId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        jpaRepository.restoreById(id);
    }
}
