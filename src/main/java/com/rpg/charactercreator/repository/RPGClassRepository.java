package com.rpg.charactercreator.repository;

import com.rpg.charactercreator.model.RPGClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 📚 RPGClassRepository
 * Repository-interface för att hantera databasanrop för RPGClass-entiteten.
 * Ger tillgång till standard CRUD-metoder och specialanpassade queries.
 */
public interface RPGClassRepository extends JpaRepository<RPGClass, Long> {

    /**
     * 🔍 Hämta en RPG-klass baserat på exakt namn (case-sensitive).
     *
     * @param name Namnet på RPG-klassen
     * @return Optional med RPGClass om den finns
     */
    Optional<RPGClass> findByName(String name);

    /**
     * 🔍 Hämta en RPG-klass med dess tillhörande färdigheter (skills) baserat på namn (case-insensitive).
     *
     * @param name Namnet på RPG-klassen
     * @return Optional med RPGClass inklusive eager-loadade färdigheter (skillList)
     */
    @Query("SELECT c FROM RPGClass c LEFT JOIN FETCH c.skillList WHERE LOWER(c.name) = LOWER(:name)")
    Optional<RPGClass> findByNameWithSkills(@Param("name") String name);
}