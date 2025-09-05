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
 * 🎮 Här styr vi alla API-anrop som har med RPG-karaktärer att göra.
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
                currentUsername,          // <- ÄGARE FRÅN SESSION
                dto.getClassName(),
                dto.getSkillIds() != null ? dto.getSkillIds() : List.of(),
                dto.getStartingItems()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.toDTO(newChar));
    }

    /**
     * 📄 Hämta alla karaktärer (med pagination).
     */
    @GetMapping
    public ResponseEntity<Page<CharacterWithDetailsDTO>> getAllCharacters(Pageable pageable) {
        return ResponseEntity.ok(
                characterService.getAllCharactersWithDetails(pageable)
        );
    }

    /**
     * 🔍 Sök karaktärer med namn (case-insensitive).
     */
    @GetMapping("/search")
    public ResponseEntity<List<CharacterWithDetailsDTO>> searchCharacters(@RequestParam String name) {
        return ResponseEntity.ok(
                characterService.searchByNameWithDetails(name)
        );
    }

    /**
     * 🔍 Hämta alla karaktärer för en viss användare.
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
     * ❌ Radera karaktär via ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id, Authentication authentication) {
        // hitta karaktär med id
        var optChar = characterService.getAllCharacters().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (optChar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var character = optChar.get();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        if (!isAdmin) {
            String currentUsername = authentication.getName();
            var owner = character.getUser(); // assumes Character has getUser()
            if (owner == null || owner.getUsername() == null || !owner.getUsername().equals(currentUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        characterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ✏️ Uppdatera karaktärsdata.
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