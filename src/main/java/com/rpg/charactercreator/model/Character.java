package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🔮 Entity för RPG-karaktär.
 */
@Entity
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Character {

    // === 🧾 Grundläggande info ===
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String background;

    private int level = 1;

    // === ⚔️ Stats ===
    private int charisma;
    private int constitution;
    private int dexterity;
    private int intelligence;
    private int strength;
    private int wisdom;

    // === 🔗 Relationer ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private RPGClass rpgClass;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "character_skills",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // === 🎒 Inventory med relation ===
    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryItem> inventoryItems = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Character character = (Character) o;

        return Objects.equals(id, character.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", background='" + background + '\'' +
                ", level=" + level +
                ", strength=" + strength +
                ", dexterity=" + dexterity +
                ", intelligence=" + intelligence +
                ", constitution=" + constitution +
                ", wisdom=" + wisdom +
                ", charisma=" + charisma +
                ", rpgClass=" + (rpgClass != null ? rpgClass.getName() : "null") +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", inventoryItems=" + inventoryItems.stream().map(InventoryItem::getName).toList() +
                '}';
    }
}