package com.rpg.charactercreator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository; //aktiverar csrf
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF-token lagras i cookie (tillgänglig för frontend/axios)
        CookieCsrfTokenRepository csrfRepo = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfRepo.setHeaderName("X-XSRF-TOKEN"); // samma header som frontend skickar

        // Hanterar CSRF-token i request
        CsrfTokenRequestAttributeHandler reqHandler = new CsrfTokenRequestAttributeHandler();
        reqHandler.setCsrfRequestAttributeName("_csrf");

        http
                // Vilka endpoints som är öppna och vilka som kräver inloggning
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/csrf-token", "/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/login", "/logout").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/characters/**").authenticated()
                        .anyRequest().authenticated()
                )
                // 🔺Inloggningsinställningar (Spring Securitys formLogin)
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> res.setStatus(200))
                        .failureHandler((req, res, ex) -> res.setStatus(401))
                )
                // Utloggning
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                )
                // 🔺CSRF-skydd aktiverat, men vissa endpoints undantas
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepo)
                        .csrfTokenRequestHandler(reqHandler)
                        .ignoringRequestMatchers("/auth/**", "/login", "/logout")
                )
                // Lägger till filter som skickar CSRF-token som cookie
                .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
                // Tillåter CORS (från AppConfig)
                .cors(Customizer.withDefaults());

        return http.build();
    }

    // Lösenord krypteras med BCrypt
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}