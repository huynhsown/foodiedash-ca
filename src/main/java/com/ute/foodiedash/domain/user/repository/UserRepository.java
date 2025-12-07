package com.ute.foodiedash.domain.user.repository;

import com.ute.foodiedash.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByIdWithRoles(Long id);
    Optional<User> findByIdWithAddresses(Long id);
    Optional<User> findByIdWithProfile(Long id);
    Optional<User> findByIdWithAll(Long id);
    Optional<User> findByEmailWithRoles(String email);

    void softDeleteById(Long id);
    void restoreById(Long id);
}
