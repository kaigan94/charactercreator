package com.rpg.charactercreator.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * 📦 InventoryItem
 * Representerar ett föremål i en karaktärs inventory.
 * Varje item har ett namn, en beskrivning och tillhör en specifik karaktär.
 */
@Getter
@Setter
@Entity
public class InventoryItem {

    /** Unikt ID för inventory-föremålet. Genereras automatiskt. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Namn på föremålet (t.ex. "Iron Sword"). */
    private String name;

    /** Beskrivning av föremålet (t.ex. "A basic melee weapon"). */
    private String description;

    /** Den karaktär som äger detta föremål. */
    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    // Constructors
    public InventoryItem() {}

    public InventoryItem(String name, String description, Character character) {
        this.name = name;
        this.description = description;
        this.character = character;
    }
}