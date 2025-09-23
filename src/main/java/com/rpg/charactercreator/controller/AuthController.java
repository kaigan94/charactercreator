package com.rpg.charactercreator.controller;

import com.rpg.charactercreator.dto.UserDTO;
import com.rpg.charactercreator.exception.UserNotFoundException;
import com.rpg.charactercreator.model.User;
import com.rpg.charactercreator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
// Controller för autentiserings-relaterade endpoints
public class AuthController {

    private final UserService userService;

    // Injekterar UserService via konstruktorn
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Registrera en ny användare
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody com.rpg.charactercreator.dto.RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.status(201).body(userService.toDTO(user));
    }

    // Returnera den inloggade användaren (session-baserat)
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(Authentication authentication) {
        // Kolla om användaren är autentiserad
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        // Hämta användarnamn från authentication-objektet
        String username = authentication.getName();
        // Hitta användare baserat på användarnamn eller kasta exception om ingen hittas
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + "does not exist."));
        // Returnera användarens data som DTO
        return ResponseEntity.ok(userService.toDTO(user));
    }
}