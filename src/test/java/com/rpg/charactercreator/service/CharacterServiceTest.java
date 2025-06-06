package com.rpg.charactercreator.service;

// 📦 DTOs och modeller
import com.rpg.charactercreator.dto.CharacterCreateDTO;
import com.rpg.charactercreator.dto.InventoryItemDTO;
import com.rpg.charactercreator.model.RPGClass;
import com.rpg.charactercreator.model.User;

// 📦 Repositories
import com.rpg.charactercreator.repository.RPGClassRepository;
import com.rpg.charactercreator.repository.SkillRepository;
import com.rpg.charactercreator.repository.UserRepository;
import com.rpg.charactercreator.repository.InventoryItemRepository;

// 📦 JUnit & Mockito
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Enhetstest för CharacterService.
 * Testar bl.a. validering av otillåtna färdigheter vid karaktärsskapande.
 */
public class CharacterServiceTest {

    private CharacterService characterService;
    private UserRepository userRepository;
    private RPGClassRepository classRepository;
    private SkillRepository skillRepository;
    private InventoryItemRepository inventoryItemRepository;

    /**
     * 🔧 Skapar mocks innan varje test.
     */
    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        classRepository = mock(RPGClassRepository.class);
        skillRepository = mock(SkillRepository.class);
        inventoryItemRepository = mock(InventoryItemRepository.class);

        characterService = new CharacterService(
                null, userRepository, classRepository, skillRepository, inventoryItemRepository
        );
    }

    /**
     * ❌ Testar att ett undantag kastas om ogiltiga skill-IDs används för en klass.
     */
    @Test
    void createCharacter_shouldThrowIllegalArgumentException_forInvalidSkills() {
        // Arrange
        User mockUser = new User();
        mockUser.setUserId(1L);

        RPGClass mockClass = new RPGClass();
        mockClass.setName("warrior");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(classRepository.findByName("warrior")).thenReturn(Optional.of(mockClass));

        CharacterCreateDTO dto = new CharacterCreateDTO();
        dto.setName("SkillFail");

        List<Long> invalidSkillIds = List.of(999L, 1000L, 1001L); // matchar inte klassens skills
        List<InventoryItemDTO> emptyItems = List.of();

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> characterService.createCharacter(dto, 1L, "warrior", invalidSkillIds, emptyItems)
        );

        assertTrue(ex.getMessage().contains("not allowed for class"));
    }
}