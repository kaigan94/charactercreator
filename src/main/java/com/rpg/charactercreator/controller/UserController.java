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
 * 🎮 User Controller - Manage users easily!
 * Handles all user-related operations like creating, updating, and fetching users.
 * Base URL: /users
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CharacterService characterService;

    // Constructor for dependency injection
    public UserController(UserService userService, CharacterService characterService) {
        this.userService = userService;
        this.characterService = characterService;
    }

    /**
     * ➕ Create a new user
     * Accepts user details and returns the created user without the password.
     * POST /users
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(userService.toDTO(createdUser));
    }

    /**
     * 📋 Get all users
     * Returns a list of all users in the system.
     * GET /users
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
     * ✏️ Update user info
     * Update username and email for a user by their ID.
     * PUT /users/{id}
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
     * 🗑️ Delete a user
     * Remove a user from the system by their ID.
     * DELETE /users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 🔍 Find user by email
     * Retrieve user details using their email address.
     * GET /users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(userService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🎭 Get characters for a user
     * Fetch all characters linked to a specific user ID.
     * GET /users/{userId}/characters
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
