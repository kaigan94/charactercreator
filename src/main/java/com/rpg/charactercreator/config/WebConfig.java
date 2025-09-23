package com.rpg.charactercreator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Markerar klassen som en konfigurationsklass i Spring
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Tillåter frontend (localhost:5173) att prata med backend
        registry.addMapping("/**") // Gäller alla endpoints
                .allowedOrigins("http://localhost:5173") // Tillåtna origins
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Tillåtna HTTP-metoder
                .allowedHeaders("*") // Tillåt alla headers
                .allowCredentials(true); // Tillåt att cookies skickas
    }
}