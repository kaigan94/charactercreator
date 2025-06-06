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

/**
 * 🎮 Här styr vi alla API-anrop som har med RPG-karaktärer att göra.
 * Bas-URL: /characters
 */
@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * ➕ Skapa en ny karaktär.
     *
     * @param dto Data från frontend
     * @return Skapad karaktär som DTO
     */
    @PostMapping
    public ResponseEntity<CharacterWithDetailsDTO> createCharacter(@RequestBody CharacterCreateDTO dto) {
        List<Long> skillIds = dto.getSkillIds() != null ? dto.getSkillIds() : List.of();

        Character newChar = characterService.createCharacter(
                dto,
                dto.getUserId(),
                dto.getClassName(),
                skillIds,
                dto.getStartingItems()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(characterService.toDTO(newChar));
    }

    /**
     * 📄 Hämta alla karaktärer (med pagination).
     *
     * @param pageable Sida och storlek
     * @return Sida med karaktärer
     */
    @GetMapping
    public ResponseEntity<Page<CharacterWithDetailsDTO>> getAllCharacters(Pageable pageable) {
        return ResponseEntity.ok(
                characterService.getAllCharactersWithDetails(pageable)
        );
    }

    /**
     * 🔍 Sök karaktärer med namn (case-insensitive).
     *
     * @param name Namn att söka efter
     * @return Lista med matchande karaktärer
     */
    @GetMapping("/search")
    public ResponseEntity<List<CharacterWithDetailsDTO>> searchCharacters(@RequestParam String name) {
        return ResponseEntity.ok(
                characterService.searchByNameWithDetails(name)
        );
    }

    /**
     * 🔍 Hämta karaktär via ID.
     *
     * @param id Karaktärens ID
     * @return Karaktär som DTO eller 404 om ej hittad
     */
    @GetMapping("/{id}")
    public ResponseEntity<CharacterWithDetailsDTO> getCharacterById(@PathVariable Long id) {
        return characterService.getAllCharacters().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(characterService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * ❌ Radera karaktär via ID.
     *
     * @param id Karaktärens ID
     * @return 204 No Content eller 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        boolean exists = characterService.getAllCharacters().stream()
                .anyMatch(c -> c.getId().equals(id));

        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        characterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ✏️ Uppdatera karaktärsdata.
     *
     * @param id Karaktärens ID
     * @param updateDTO Fält som ska uppdateras
     * @return Uppdaterad karaktär som DTO
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