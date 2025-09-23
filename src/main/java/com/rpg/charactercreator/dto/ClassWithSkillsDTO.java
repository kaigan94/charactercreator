package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO-klass som representerar en RPG-klass med dess egenskaper och kopplade skills.
 */
@Getter
@Setter
@NoArgsConstructor
public class ClassWithSkillsDTO {

    // 📘 Klassinformation
    private String name;         // Namn på RPG-klassen
    private String description;  // Beskrivning av klassen

    // 💪 Stats
    private int strength;        // Styrka, fysisk kraft
    private int dexterity;       // Smidighet och snabbhet
    private int intelligence;    // Intelligens och magiska förmågor
    private int constitution;    // Uthållighet och hälsa
    private int wisdom;          // Visdom och insikt
    private int charisma;        // Karisma och social förmåga

    private String role;             // Roll i gruppen, t.ex. "Tank", "Healer"
    private String armorType;        // Typ av rustning, t.ex. "Plate", "Cloth"
    private List<String> weapons;    // Lista över tillgängliga vapen

    // kopplade skills
    private List<SkillDTO> skills;   // Lista över kopplade färdigheter (skills)
}