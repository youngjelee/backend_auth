package com.backend.ljy.auth.api.repository;

import com.backend.ljy.auth.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , UserRepositoryCustom {
    Optional<User> findByUserid(String username);
}