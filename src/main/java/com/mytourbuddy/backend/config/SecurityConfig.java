package com.mytourbuddy.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mytourbuddy.backend.security.CustomUserDetailsService;
import com.mytourbuddy.backend.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // .requestMatchers("/api/v1/bookings/**").permitAll()

                        // users
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")

                        // packages
                        .requestMatchers(HttpMethod.GET, "/api/v1/packages/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/packages").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/packages/**").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/packages/**").hasAnyRole("GUIDE", "ADMIN")

                        // experiences
                        .requestMatchers(HttpMethod.GET, "/api/v1/experiences/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/experiences").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/experiences/**").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/experiences/**").hasAnyRole("GUIDE", "ADMIN")

                        // reviews
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/reviews/**").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").hasAnyRole("TOURIST", "ADMIN")

                        // bookings
                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings/my").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings/guide").hasRole("GUIDE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings").hasRole("TOURIST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/bookings/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings/**").authenticated()
                        .requestMatchers("/api/v1/bookings/**").authenticated()

                        // tickets
                        .requestMatchers(HttpMethod.GET, "/api/v1/tickets/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tickets").hasAnyRole("TOURIST", "GUIDE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tickets/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tickets/**").authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}