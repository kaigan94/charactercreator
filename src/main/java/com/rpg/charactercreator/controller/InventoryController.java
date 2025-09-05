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
@RequestMapping("/characters/{characterId}/inventory")
public class InventoryController {

    private final CharacterService characterService;

    public InventoryController(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * ➕ POST /characters/{characterId}/inventory
     * Lägger till ett nytt item till en karaktärs inventory.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addItem(@PathVariable Long characterId, @RequestBody InventoryItemDTO itemDTO) {
        if (itemDTO.getName() == null || itemDTO.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        characterService.addInventoryItem(characterId, itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 📄 GET /characters/{characterId}/inventory
     * Hämtar inventory-listan för en viss karaktär.
     */
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getInventory(@PathVariable Long characterId) {
        List<InventoryItemDTO> inventory = characterService.getInventoryForCharacter(characterId);
        return ResponseEntity.ok(inventory);
    }
}