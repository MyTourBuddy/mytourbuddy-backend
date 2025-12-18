package com.mytourbuddy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // get user by id
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // create user
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!checkProfile(user)) {
            throw new IllegalArgumentException("All fields are required and must not be empty.");
        }

        user.setIsProfileComplete(false);
        user.setMemberSince(Instant.now());

        return userRepository.save(user);
    }

    // verify user before create
    private boolean checkProfile(User user) {
        // common
        if (user.getFirstName() == null || user.getFirstName().isEmpty())
            return false;
        if (user.getLastName() == null || user.getLastName().isEmpty())
            return false;
        if (user.getEmail() == null || user.getEmail().isEmpty())
            return false;
        if (user.getAge() == null || user.getAge() < 1 || user.getAge() > 150)
            return false;
        if (user.getUsername() == null || user.getUsername().isEmpty())
            return false;
        if (user.getPassword() == null || user.getPassword().isEmpty())
            return false;

        if (user.getRole() == Role.TOURIST) {
            return user.getCountry() != null && !user.getCountry().isEmpty()
                    && user.getTravelPreferences() != null
                    && !user.getTravelPreferences().isEmpty();
        }

        if (user.getRole() == Role.GUIDE) {
            return user.getLanguages() != null
                    && !user.getLanguages().isEmpty()
                    && user.getYearsOfExp() != null
                    && user.getYearsOfExp() >= 0;
        }

        return user.getRole() == Role.ADMIN;
    }

    // TODO
    // private boolean checkIsProfileComplete(User user){
    //     return true;
    // }

}
