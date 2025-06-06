package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * 📦 StartingItem
 * Entity representing a predefined item that a player can start with,
 * associated with a specific RPG class.
 */
@Entity
@Table(name = "starting_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartingItem {

    /**
     * 🔑 Unique identifier for the item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 🏷️ Name of the starting item.
     */
    private String name;

    /**
     * 📝 Description of what the item does or represents.
     */
    private String description;

    /**
     * 🔗 The RPG class this item is associated with.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private RPGClass rpgClass;
}