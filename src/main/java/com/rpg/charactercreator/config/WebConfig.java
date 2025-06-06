package com.rpg.charactercreator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 🌐 CORS-inställningar för att frontend (t.ex. React) ska kunna prata med backend.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                         // Tillåt alla endpoints
                .allowedOrigins("http://localhost:5173")   // Tillåt bara från vår frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Tillåt dessa metoder
                .allowedHeaders("*")                       // Tillåt alla headers
                .allowCredentials(true);                   // Tillåt cookies och auth-info
    }
}