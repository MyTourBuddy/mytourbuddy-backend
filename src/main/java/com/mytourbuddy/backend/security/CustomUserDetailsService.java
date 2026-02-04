package com.mytourbuddy.backend.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user));
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}