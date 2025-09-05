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
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody com.rpg.charactercreator.dto.RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.status(201).body(userService.toDTO(user));
    }

    // returnera inloggad user (session-baserat)
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user with username: " + username));

        return ResponseEntity.ok(userService.toDTO(user));
    }
}