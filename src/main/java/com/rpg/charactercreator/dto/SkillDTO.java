package com.rpg.charactercreator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 🧠 SkillDTO
 * DTO som representerar en färdighet i spelet.
 * Används för att skicka skill-data mellan backend och frontend utan att exponera hela Skill-entiteten.
 */
@Getter
@Setter
@AllArgsConstructor
public class SkillDTO {

    /** Unikt ID för färdigheten */
    private Long id;

    /** Namn på färdigheten */
    private String name;

    /** Beskrivning av vad färdigheten gör */
    private String description;
}