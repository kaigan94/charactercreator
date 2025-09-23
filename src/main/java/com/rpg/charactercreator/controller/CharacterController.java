package com.rpg.charactercreator.controller;

import com.rpg.charactercreator.dto.CharacterCreateDTO;
import com.rpg.charactercreator.dto.CharacterUpdateDTO;
import com.rpg.charactercreator.dto.CharacterWithDetailsDTO;
import com.rpg.charactercreator.model.Character;
import com.rpg.charactercreator.service.CharacterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

/**
 * 🎮 Controller för RPG-karaktärer.
 * Hanterar API-anrop relaterade till karaktärer.
 * Bas-URL: /characters
 */
@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService,
                               com.rpg.charactercreator.service.UserService userService) {
        this.characterService = characterService;
    }

    /**
     * Skapar en ny karaktär kopplad till inloggad användare.
     */
    @PostMapping
    public ResponseEntity<CharacterWithDetailsDTO> createCharacter(
            @RequestBody CharacterCreateDTO dto,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String currentUsername = authentication.getName();

        Character newChar = characterService.createCharacterForUsername(
                dto,
                currentUsername,
                dto.getClassName(),
                dto.getSkillIds() != null ? dto.getSkillIds() : List.of(),
                dto.getStartingItems()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.toDTO(newChar));
    }

    /**
     * 📄 Hämtar alla karaktärer med pagination.
     */
    @GetMapping
    public ResponseEntity<Page<CharacterWithDetailsDTO>> getAllCharacters(Pageable pageable) {
        return ResponseEntity.ok(
                characterService.getAllCharactersWithDetails(pageable)
        );
    }

    /**
     * 🔍 Söker karaktärer på namn (case-insensitive).
     */
    @GetMapping("/search")
    public ResponseEntity<List<CharacterWithDetailsDTO>> searchCharacters(@RequestParam String name) {
        return ResponseEntity.ok(
                characterService.searchByNameWithDetails(name)
        );
    }

    /**
     * 🔍 Hämtar alla karaktärer för en specifik användare.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CharacterWithDetailsDTO>> getCharactersByUserId(@PathVariable Long userId) {
        List<Character> characters = characterService.getCharactersByUserId(userId);
        List<CharacterWithDetailsDTO> dtos = characters.stream()
                .map(characterService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * ❌ Tar bort en karaktär via dess ID.
     * Endast ägare eller admin kan ta bort.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        Character character = characterService.getById(id); // kastar 404 om ej finns

        if (!isAdmin) {
            String currentUsername = authentication.getName();
            var owner = character.getUser();
            if (owner == null || owner.getUsername() == null || !owner.getUsername().equals(currentUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        characterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ✏️ Uppdaterar data för en befintlig karaktär.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CharacterWithDetailsDTO> updateCharacter(
            @PathVariable Long id,
            @RequestBody CharacterUpdateDTO updateDTO
    ) {
        Character updated = characterService.updateCharacter(id, updateDTO);
        return ResponseEntity.ok(characterService.toDTO(updated));
    }
}