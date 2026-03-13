package com.ute.foodiedash.infrastructure.persistence.user.adapter;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper.UserJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    @Transactional
    public User save(User user) {
        try {
            UserJpaEntity jpaEntity;
            if (user.getId() == null) {
                jpaEntity = userJpaMapper.toJpaEntity(user);
            } else {
                UserJpaEntity existing = userJpaRepository.findById(user.getId())
                        .orElseThrow(() -> new NotFoundException("User not found"));

                userJpaMapper.updateJpaEntity(user, existing);
                jpaEntity = existing;
            }

            UserJpaEntity saved = userJpaRepository.save(jpaEntity);
            return userJpaMapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains("uq_users_email")) {
                throw new BadRequestException("Email already exists");
            }
            if (e.getMessage() != null && e.getMessage().contains("uq_users_phone")) {
                throw new BadRequestException("Phone already exists");
            }
            throw e;
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("User was modified by another transaction");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmailWithRoles(String email) {
        return userJpaRepository.findByEmailWithRoles(email)
                .map(userJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdWithRoles(Long id) {
        return userJpaRepository.findByIdWithRoles(id)
                .map(userJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdWithAddresses(Long id) {
        return userJpaRepository.findByIdWithAddresses(id)
                .map(userJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdWithProfile(Long id) {
        return userJpaRepository.findByIdWithProfile(id)
                .map(userJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdWithAll(Long id) {
        return userJpaRepository.findByIdWithAll(id)
                .map(userJpaMapper::toDomain);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        userJpaRepository.softDeleteById(id);
    }

    @Override
    @Transactional
    public void restoreById(Long id) {
        userJpaRepository.restoreById(id);
    }
}
