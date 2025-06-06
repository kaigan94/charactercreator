package com.rpg.charactercreator.repository;

import com.rpg.charactercreator.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository-interface för entiteten Skill.
 * Tillhandahåller standardmetoder för att läsa, spara och ta bort färdigheter (skills) från databasen.
 * Genom att ärva från JpaRepository får vi tillgång till metoder som:
 * - findAll()
 * - findById()
 * - save()
 * - deleteById()
 */
public interface SkillRepository extends JpaRepository<Skill, Long> {
    // Long = Datatypen för ID:t i Skill-entiteten
}