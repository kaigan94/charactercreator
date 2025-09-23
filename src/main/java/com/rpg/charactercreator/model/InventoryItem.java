package com.rpg.charactercreator.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity // Markeras som en databas-entitet
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-ID i databasen
    private Long id;

    private String name;        // Namn på föremålet
    private String description; // Kort beskrivning av föremålet

    @ManyToOne // Många items kan tillhöra en karaktär
    @JoinColumn(name = "character_id", nullable = false) // Koppling till Character-tabellen
    private Character character;

    // Standardkonstruktor (krävs av JPA)
    public InventoryItem() {}

    // Konstruktor för att snabbt skapa nya items
    public InventoryItem(String name, String description, Character character) {
        this.name = name;
        this.description = description;
        this.character = character;
    }
}