package com.mytourbuddy.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin

public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUserById(id).orElse(null);

    }
    @GetMapping("/guides")
    public List<User> getGuides() {
        return userService.getAllGuides();
    }
    @GetMapping("/tourists")
    public List<User> getTourists() {
        return userService.getAllTourists();
    }



}