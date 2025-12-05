package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.MenuItemOptionJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.MenuItemOptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuItemOptionRepositoryAdapter implements MenuItemOptionRepository {
    private final MenuItemOptionJpaRepository jpaRepository;
    private final MenuItemOptionJpaMapper mapper;

    @Override
    public MenuItemOption save(MenuItemOption option) {
        MenuItemOptionJpaEntity jpaEntity = mapper.toJpaEntity(option);
        MenuItemOptionJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MenuItemOption> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<MenuItemOption> findByMenuItemIdAndDeletedAtIsNull(Long menuItemId) {
        return jpaRepository.findByMenuItemIdAndDeletedAtIsNull(menuItemId).stream()
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
