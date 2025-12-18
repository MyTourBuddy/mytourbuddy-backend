package com.mytourbuddy.backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        user.setProfileComplete(checkProfile(user));
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> getAllGuides() {
        return userRepository.findByRole("guide");
    }

    public List<User> getAllTourists() {
        return userRepository.findByRole("tourist");
    }

    private boolean checkProfile(User user) {
        return user.getFirstName() != null &&
                user.getLastName() != null &&
                user.getEmail() != null &&
                user.getUsername() != null &&
                user.getPassword() != null;
    }
}
