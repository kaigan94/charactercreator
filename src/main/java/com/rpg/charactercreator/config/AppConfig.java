package com.rpg.charactercreator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setMaxAge(3600L); // cache preflight
        config.setAllowedOrigins(List.of("http://localhost:5173")); // tillåt frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // tillåt metoder
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-XSRF-TOKEN")); // tillåt headers
        config.setAllowCredentials(true); // tillåt cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // gäller alla endpoints
        return source;
    }
}