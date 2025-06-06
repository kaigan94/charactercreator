package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 🧙 ClassWithSkillsDTO
 * Innehåller klassnamn, beskrivning, start-stats, roll, rustningstyp, vapen och kopplade skills.
 */
@Getter
@Setter
@NoArgsConstructor
public class ClassWithSkillsDTO {

    // 📘 Klassinformation
    private String name;         // Namn på RPG-klassen
    private String description;  // Beskrivning

    // 💪 Stats
    private int strength;
    private int dexterity;
    private int intelligence;
    private int constitution;
    private int wisdom;
    private int charisma;

    // 🛡️ Extra klassinfo
    private String role;             // T.ex. "Tank", "Healer"
    private String armorType;        // T.ex. "Plate", "Cloth"
    private List<String> weapons;    // T.ex. ["Sword", "Shield"]

    // 🎯 Kopplade skills
    private List<SkillDTO> skills;
}