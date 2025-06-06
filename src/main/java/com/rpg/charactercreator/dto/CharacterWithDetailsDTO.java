package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 🧙 CharacterWithDetailsDTO
 * En DTO som innehåller all viktig information om en karaktär.
 * Den används för att visa detaljer som name, stats, skills och user.
 * Innehåller även klassens roll, rustningstyp och vapen.
 */

@Getter
@Setter
@NoArgsConstructor
public class CharacterWithDetailsDTO {

    // 🧾 Grundläggande information om karaktären
    private Long id;             // Unikt ID för karaktären
    private String name;         // Namn på karaktären
    private String background;   // Bakgrundshistoria eller lore
    private int level;           // Karaktärens level (startar på 1)

    // 💪 Karaktärens bas-statistik
    private int strength;        // Fysisk styrka
    private int dexterity;       // Smidighet och snabbhet
    private int intelligence;    // Intellekt och magikunskaper
    private int constitution;    // Health/HP och uthållighet
    private int wisdom;          // Visdom och beslutsförmåga
    private int charisma;        // Social förmåga och charm

    // 🎒 Startföremål eller inventory för karaktären
    private List<String> inventory;  // Lista med startföremålen

    // 🔗 Kopplingar till andra entiteter
    private String className;    // Namn på karaktärens RPG-klass
    private String role;         // Roll för klassen (t.ex. tank, healer)
    private String armorType;    // Typ av rustning (t.ex. cloth, plate)
    private List<String> weapons; // Lista med vapen associerade med klassen
    private String username;     // Användarnamn för skaparen
    private List<String> skills; // Lista med valda skills
}
