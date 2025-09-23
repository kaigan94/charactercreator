package com.rpg.charactercreator.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rpg.charactercreator.dto.CharacterWithDetailsDTO;
import com.rpg.charactercreator.dto.UserDTO;
import com.rpg.charactercreator.model.Character;
import com.rpg.charactercreator.model.User;
import com.rpg.charactercreator.service.CharacterService;
import com.rpg.charactercreator.service.UserService;

/**
 * 🎮 UserController
 * Hanterar allt som rör användare (skapa, hämta, uppdatera, ta bort).
 * Bas-URL: /users
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CharacterService characterService;

    // Konstruktor-injektion av services
    public UserController(UserService userService, CharacterService characterService) {
        this.userService = userService;
        this.characterService = characterService;
    }

    /**
     * ➕ POST /users
     * Skapar en ny användare (lösenord exkluderas i DTO).
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(userService.toDTO(createdUser));
    }

    /**
     * 📋 GET /users
     * Hämtar alla användare som DTO-lista.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> dtoList = users.stream()
                .map(userService::toDTO)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * ✏️ PUT /users/{id}
     * Uppdaterar username och email för en användare.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    User savedUser = userService.createUser(existingUser);
                    return ResponseEntity.ok(userService.toDTO(savedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🗑️ DELETE /users/{id}
     * Tar bort en användare med angivet ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 🔍 GET /users/email/{email}
     * Hämtar användare baserat på email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(userService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🎭 GET /users/{userId}/characters
     * Hämtar alla karaktärer kopplade till en viss användare.
     */
    @GetMapping("/{userId}/characters")
    public ResponseEntity<List<CharacterWithDetailsDTO>> getCharactersByUserId(@PathVariable Long userId) {
        List<Character> characters = characterService.getCharactersByUserId(userId);
        List<CharacterWithDetailsDTO> dtos = characters.stream()
                .map(characterService::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}