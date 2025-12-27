package com.ute.foodiedash.infrastructure.persistence.menu.adapter;

import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionValueRepository;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper.MenuItemOptionValueJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.repository.MenuItemOptionValueJpaRepository;
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
        return com.ute.foodiedash.domain.menu.model.MenuItemOptionValue.reconstruct(
                saved.getId(),
                saved.getOption() != null ? saved.getOption().getId() : null,
                saved.getName(),
                saved.getExtraPrice(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getCreatedBy(),
                saved.getUpdatedBy(),
                saved.getDeletedAt(),
                saved.getVersion()
        );
    }

    @Override
    public Optional<MenuItemOptionValue> findById(Long id) {
        return jpaRepository.findById(id)
            .map(saved -> com.ute.foodiedash.domain.menu.model.MenuItemOptionValue.reconstruct(
                    saved.getId(),
                    saved.getOption() != null ? saved.getOption().getId() : null,
                    saved.getName(),
                    saved.getExtraPrice(),
                    saved.getCreatedAt(),
                    saved.getUpdatedAt(),
                    saved.getCreatedBy(),
                    saved.getUpdatedBy(),
                    saved.getDeletedAt(),
                    saved.getVersion()
            ));
    }

    @Override
    public List<MenuItemOptionValue> findByOptionIdAndDeletedAt(Long optionId, Instant deletedAt) {
        return jpaRepository.findByOptionIdAndDeletedAt(optionId, deletedAt).stream()
            .map(saved -> com.ute.foodiedash.domain.menu.model.MenuItemOptionValue.reconstruct(
                    saved.getId(),
                    saved.getOption() != null ? saved.getOption().getId() : null,
                    saved.getName(),
                    saved.getExtraPrice(),
                    saved.getCreatedAt(),
                    saved.getUpdatedAt(),
                    saved.getCreatedBy(),
                    saved.getUpdatedBy(),
                    saved.getDeletedAt(),
                    saved.getVersion()
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemOptionValue> findByOptionIdInAndDeletedAtIsNull(List<Long> optionIds) {
        return jpaRepository.findByOptionIdInAndDeletedAtIsNull(optionIds).stream()
            .map(saved -> com.ute.foodiedash.domain.menu.model.MenuItemOptionValue.reconstruct(
                    saved.getId(),
                    saved.getOption() != null ? saved.getOption().getId() : null,
                    saved.getName(),
                    saved.getExtraPrice(),
                    saved.getCreatedAt(),
                    saved.getUpdatedAt(),
                    saved.getCreatedBy(),
                    saved.getUpdatedBy(),
                    saved.getDeletedAt(),
                    saved.getVersion()
            ))
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
