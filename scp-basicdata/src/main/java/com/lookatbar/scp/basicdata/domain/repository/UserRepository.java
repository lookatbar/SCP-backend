package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    List<User> findAll();

    List<User> findByStatus(Integer status);

    void deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}