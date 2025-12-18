package com.mytourbuddy.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytourbuddy.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
}
