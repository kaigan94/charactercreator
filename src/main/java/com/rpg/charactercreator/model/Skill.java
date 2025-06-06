package com.rpg.charactercreator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📦 Entity som representerar en färdighet (skill) kopplad till en RPG-klass.
 * Används för att beskriva vad en karaktär kan göra (t.ex. "Fireball", "Stealth").
 */
@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
public class Skill {

    /**
     * 🔑 Primärnyckel för skill.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 🏷️ Namn på färdigheten.
     */
    private String name;

    /**
     * 📝 Beskrivning av vad färdigheten gör.
     */
    private String description;

    /**
     * 🎭 Den RPG-klass som färdigheten tillhör.
     */
    @ManyToOne
    @JoinColumn(name = "class_id")  // matchar din kolumn i databasen
    @JsonIgnore
    private RPGClass rpgClass;

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}