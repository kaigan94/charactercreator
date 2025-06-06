package com.rpg.charactercreator.dto;

import lombok.*;

/**
 * 📦 StartingItemDTO
 * Data Transfer Object used to transfer information about a starting item
 * that can be assigned to a character during creation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartingItemDTO {

    /** The name of the starting item (e.g., "Iron Sword", "Healing Potion"). */
    private String name;

    /** A short description of what the item is or does. */
    private String description;
}