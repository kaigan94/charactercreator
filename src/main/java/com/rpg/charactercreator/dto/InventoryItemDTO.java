/**
 * DTO som används för att representera inventory-items.
 */
package com.rpg.charactercreator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {

    /** Namnet på föremålet */
    private String name;
    /** Beskrivning av föremålet */
    private String description;
}