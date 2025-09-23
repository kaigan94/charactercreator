package com.rpg.charactercreator.controller;

import com.rpg.charactercreator.dto.InventoryItemDTO;
import com.rpg.charactercreator.service.CharacterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 🎒 InventoryController
 * Hanterar endpoints för att lägga till och hämta inventory-items för en karaktär.
 */
@RestController
@RequestMapping("/characters/{characterId}/inventory") // Alla endpoints gäller för ett specifikt characterId
public class InventoryController {

    private final CharacterService characterService;

    // Dependency injection av CharacterService
    public InventoryController(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * ➕ POST /characters/{characterId}/inventory
     * Lägger till ett nytt item i en karaktärs inventory.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Returnerar 201 vid lyckad skapning
    public ResponseEntity<Void> addItem(@PathVariable Long characterId, @RequestBody InventoryItemDTO itemDTO) {
        // Enkel validering: namn får inte vara null eller tomt
        if (itemDTO.getName() == null || itemDTO.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        characterService.addInventoryItem(characterId, itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    /**
     * 📄 GET /characters/{characterId}/inventory
     * Hämtar alla items i en karaktärs inventory.
     */
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getInventory(@PathVariable Long characterId) {
        List<InventoryItemDTO> inventory = characterService.getInventoryForCharacter(characterId);
        return ResponseEntity.ok(inventory); // 200 OK med inventory-listan
    }
}