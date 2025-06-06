package com.rpg.charactercreator.repository;

import com.rpg.charactercreator.model.StartingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 📦 Repository för att hantera databasanrop för startföremål (StartingItem).
 * Används för att hämta och spara startföremål kopplade till olika RPG-klasser.
 */
public interface StartingItemRepository extends JpaRepository<StartingItem, Long> {

    /**
     * 🔍 Hämta alla startföremål som tillhör en viss klass, oavsett skiftläge.
     *
     * @param className Namnet på RPG-klassen
     * @return Lista med matchande startföremål
     */
    List<StartingItem> findByRpgClassNameIgnoreCase(String className);
}