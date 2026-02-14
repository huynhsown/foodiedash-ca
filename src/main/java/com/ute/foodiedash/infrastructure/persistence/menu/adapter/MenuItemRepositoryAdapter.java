package com.ute.foodiedash.infrastructure.persistence.menu.adapter;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper.MenuItemJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.repository.MenuItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuItemRepositoryAdapter implements MenuItemRepository {
    private final MenuItemJpaRepository jpaRepository;
    private final MenuItemJpaMapper mapper;

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemJpaEntity jpaEntity = mapper.toJpaEntity(menuItem);
        MenuItemJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<MenuItem> findByOptionId(Long optionId) {
        return jpaRepository.findByOptionId(optionId)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<MenuItem> findByIdInAndDeletedAtIsNull(List<Long> menuItemIds) {
        return jpaRepository.findByIdInAndDeletedAtIsNull(menuItemIds).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findByMenuIdInAndDeletedAtIsNull(List<Long> menuIds) {
        return jpaRepository.findByMenuIdInAndDeletedAtIsNull(menuIds).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findByMenuId(Long menuId, boolean includeDeleted) {
        return jpaRepository.findByMenuId(menuId, includeDeleted).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId, boolean includeDeleted) {
        return jpaRepository.findByRestaurantId(restaurantId, includeDeleted).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByMenuIdAndDeletedAtIsNull(Long menuId) {
        return jpaRepository.existsByMenuIdAndDeletedAtIsNull(menuId);
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
