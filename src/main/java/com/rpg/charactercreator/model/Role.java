package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity @Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false)
    private String name; // "ROLE_USER", "ROLE_ADMIN"

    public Role() {}
    public Role(String name) { this.name = name; }

}