package com.rpg.charactercreator.repository;

import com.rpg.charactercreator.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 📦 InventoryItemRepository
 * Repository för att hantera inventory-items kopplade till karaktärer.
 * Använder Spring Data JPA för att kommunicera med databasen.
 */
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    /**
     * 🔍 Hämta alla inventory-items som tillhör en specifik karaktär.
     */
    List<InventoryItem> findByCharacterId(Long characterId);
}