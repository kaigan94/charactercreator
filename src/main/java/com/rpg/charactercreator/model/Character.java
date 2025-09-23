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

@Entity
@Table(name = "characters") // Kopplas till tabellen "characters" i databasen
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-ID från databasen
    private Long id;

    @NotBlank // Namn får inte vara tomt
    private String name;

    private String background; // Kort bakgrundsbeskrivning
    private int level = 1;     // Alla startar på level 1

    // === ⚔️ Stats för spelet ===
    private int charisma;
    private int constitution;
    private int dexterity;
    private int intelligence;
    private int strength;
    private int wisdom;

    // === 🔗 Relationer till andra entiteter ===
    @ManyToOne(fetch = FetchType.LAZY) // En klass kan ha många karaktärer
    @JoinColumn(name = "class_id")
    private RPGClass rpgClass;

    @ManyToMany(fetch = FetchType.LAZY) // En karaktär kan ha många skills
    @JoinTable(
            name = "character_skills",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY) // En användare kan äga flera karaktärer
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // 🎒 Koppling till inventory-items (ett-till-många)
    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryItem> inventoryItems = new ArrayList<>();

    // Jämförelse av karaktärer baserat på ID
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

    // toString() för enkel utskrift/debugging
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