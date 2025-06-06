package com.rpg.charactercreator.repository;

import com.rpg.charactercreator.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository-gränssnitt för att hantera Character-entiteter i databasen.
 */
public interface CharacterRepository extends JpaRepository<Character, Long> {

    /**
     * Söker efter karaktärer vars namn innehåller den angivna strängen (skiftlägesokänsligt).
     *
     * @param name Del av karaktärens namn att söka efter
     * @return Lista med matchande karaktärer
     */
    List<Character> findByNameContainingIgnoreCase(String name);
}