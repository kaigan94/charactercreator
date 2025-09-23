package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "starting_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private RPGClass rpgClass;
}