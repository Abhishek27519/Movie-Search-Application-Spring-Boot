package com.example.moviesearchapp.repository;

import com.example.moviesearchapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Data access layer for User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to find a user by their unique username
    Optional<User> findByUsername(String username);
}