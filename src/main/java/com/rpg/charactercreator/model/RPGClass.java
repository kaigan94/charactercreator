package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RPGClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;
    private String name;
    private String description;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int constitution;
    private int wisdom;
    private int charisma;

    @OneToMany(mappedBy = "rpgClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Skill> skillList;
    private String armorType;
    private String role;

    @ElementCollection
    @CollectionTable(name = "class_weapons", joinColumns = @JoinColumn(name = "class_id"))
    @Column(name = "weapon")
    private List<String> weapons = new ArrayList<>();
    private String startingWeapon;

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