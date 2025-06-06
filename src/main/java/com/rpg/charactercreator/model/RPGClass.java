package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a character class in an RPG game, defining its attributes and associated skills.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RPGClass {

    /** Unique identifier for the RPG class */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    /** Name of the RPG class */
    private String name;

    /** Description of the RPG class */
    private String description;

    /** Strength attribute representing physical power */
    private int strength;

    /** Dexterity attribute representing agility and reflexes */
    private int dexterity;

    /** Intelligence attribute representing mental acuity */
    private int intelligence;

    /** Constitution attribute representing endurance and health */
    private int constitution;

    /** Wisdom attribute representing perception and insight */
    private int wisdom;

    /** Charisma attribute representing charm and influence */
    private int charisma;

    /** List of skills associated with this RPG class */
    @OneToMany(mappedBy = "rpgClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Skill> skillList;

    /** The armor type typically worn by this class (e.g., light, medium, heavy) */
    private String armorType;

    /** The role this class plays (e.g., tank, healer, damage dealer) */
    private String role;

    /** A list of weapon names commonly associated with this class */
    @ElementCollection
    @CollectionTable(name = "class_weapons", joinColumns = @JoinColumn(name = "class_id"))
    @Column(name = "weapon")
    private List<String> weapons = new ArrayList<>();

    @Override
    public String toString() {
        return "RPGClass{" +
                "classId=" + classId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", armorType='" + armorType + '\'' +
                ", role='" + role + '\'' +
                ", skillCount=" + (skillList != null ? skillList.size() : 0) +
                ", weaponCount=" + (weapons != null ? weapons.size() : 0) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RPGClass rpgClass)) return false;
        return Objects.equals(classId, rpgClass.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId);
    }
}