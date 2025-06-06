package com.rpg.charactercreator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 🎒 InventoryItemDTO
 * En DTO som representerar ett föremål i en karaktärs inventory.
 * Används när man skickar eller tar emot data om items mellan backend och frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {

    /** Namnet på föremålet (t.ex. "Iron Sword") */
    private String name;

    /** En kort beskrivning av vad föremålet är eller gör */
    private String description;
}