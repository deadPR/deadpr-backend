package com.deadpr.backend.repository;

import com.deadpr.backend.model.Role;
import com.deadpr.backend.model.User;
import org.springframework.data.domain.Pageable; // Naya import
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List; // Naya import
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    @Query(value = "{ 'role' : ?0 }", count = true)
    long countByRole(Role role);
    List<User> findByRole(Role role, Pageable pageable);

}
