package com.rpg.charactercreator.config;

// Den här filen innehåller inställningar för säkerhet och CORS, alltså vilka som får prata med vår backend och hur lösenord krypteras.

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Den här klassen används för att samla alla inställningar kring säkerhet och CORS i vår applikation.
 * Det gör det lättare att hålla koll på och ändra säkerhetsinställningar på ett ställe.
 */

@Configuration
public class AppConfig {

    /**
     * Här skapar vi en bean som används för att kryptera lösenord med BCrypt.
     * Det gör att vi aldrig sparar lösenord som vanlig text, vilket är säkrare.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Här bestämmer vi hur säkerheten ska funka i vår app just nu.
     * Vi tillåter alla att nå alla endpoints, stänger av CSRF-skydd (för enkelhets skull),
     * och aktiverar CORS så att vår frontend kan prata med backend.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    /**
     * Här ställer vi in CORS så att frontend som körs på localhost:5173 får göra anrop till backend.
     * Vi specificerar vilka metoder och headers som är tillåtna för bättre kontroll och säkerhet.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173");

        // Tillåtna HTTP-metoder:
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");

        // Tillåtna headers:
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}