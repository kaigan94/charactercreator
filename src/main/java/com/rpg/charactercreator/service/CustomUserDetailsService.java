package com.rpg.charactercreator.service;

import com.rpg.charactercreator.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
// Serviceklass för att ladda användardetaljer vid autentisering
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;

    // Konstruktor som injicerar UserRepository för att hämta användare från databasen
    public CustomUserDetailsService(UserRepository users) { this.users = users; }

    // Metod som laddar användardetaljer baserat på användarnamn
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Hämtar användaren från databasen eller kastar undantag om användaren inte finns
        var u = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Mappning av användarens roller till Spring Securitys GrantedAuthority för behörigheter
        var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .toList();
        // Skapar och returnerar en UserDetails-instans med användarens information och behörigheter
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), u.isEnabled(),
                true, true, true, authorities
        );
    }
}