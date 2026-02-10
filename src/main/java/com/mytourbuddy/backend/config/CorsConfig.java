package com.mytourbuddy.backend.config;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow cookies to be sent with cross-origin requests
        config.setAllowCredentials(true);

        // Set allowed frontend origins (from application.properties)
        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // Allow all request headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // Specify which HTTP methods are permitted
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Expose these headers to the frontend
        config.setExposedHeaders(Arrays.asList(
                "Set-Cookie",
                "Authorization",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));

        // Cache preflight requests for 3 days
        config.setMaxAge(Duration.ofDays(3));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
