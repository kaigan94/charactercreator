package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * 🎯 CharacterDTO
 * En enkel DTO för att visa grundläggande info om en karaktär.
 * Används när man inte behöver visa användare eller klassdetaljer.
 */
@Getter
@Setter
@NoArgsConstructor
public class CharacterDTO {

    /** Karaktärens namn */
    private String name;

    /** Karaktärens nivå (level) */
    private int level;
}
