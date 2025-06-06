package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 🛠 DTO för att uppdatera en karaktär.
 * Används t.ex. när man skickar in ändringar via PUT /characters/{id}.
 */
@Getter
@Setter
@NoArgsConstructor
public class CharacterUpdateDTO {

    /** Karaktärens strength (STR) */
    private Integer strength;

    /** Karaktärens dexterity (DEX) */
    private Integer dexterity;

    /** Karaktärens intelligence (INT) */
    private Integer intelligence;

    /** Karaktärens health (CON) */
    private Integer constitution;

    /** Karaktärens visdom (WIS) */
    private Integer wisdom;

    /** Karaktärens karisma (CHA) */
    private Integer charisma;

    /** Lista med ID:n för valda skills */
    private List<Long> skillIds;

    /** Kort text om karaktärens bakgrund */
    private String background;

    /** Karaktärens namn */
    private String name;

    /** Namnet på den klass karaktären ska tillhöra */
    private String className;
}