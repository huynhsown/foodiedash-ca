package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionValueRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuItemOptionValueJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.MenuItemOptionValueJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.MenuItemOptionValueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuItemOptionValueRepositoryAdapter implements MenuItemOptionValueRepository {
    private final MenuItemOptionValueJpaRepository jpaRepository;
    private final MenuItemOptionValueJpaMapper mapper;

    @Override
    public MenuItemOptionValue save(MenuItemOptionValue optionValue) {
        MenuItemOptionValueJpaEntity jpaEntity = mapper.toJpaEntity(optionValue);
        MenuItemOptionValueJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MenuItemOptionValue> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<MenuItemOptionValue> findByOptionIdAndDeletedAt(Long optionId, Instant deletedAt) {
        return jpaRepository.findByOptionIdAndDeletedAt(optionId, deletedAt).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemOptionValue> findByOptionIdInAndDeletedAtIsNull(List<Long> optionIds) {
        return jpaRepository.findByOptionIdInAndDeletedAtIsNull(optionIds).stream()
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
