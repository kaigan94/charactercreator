package com.rpg.charactercreator.service;

import com.rpg.charactercreator.exception.CharacterNotFoundException;
import com.rpg.charactercreator.exception.SkillNotFoundException;
import com.rpg.charactercreator.model.Character;
import com.rpg.charactercreator.model.Skill;
import com.rpg.charactercreator.repository.CharacterRepository;
import com.rpg.charactercreator.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 🎯 SkillService
 * Service-klass som hanterar logiken för att hämta, spara, radera och koppla färdigheter (skills) till karaktärer.
 */
@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;

    public SkillService(SkillRepository skillRepository, CharacterRepository characterRepository) {
        this.skillRepository = skillRepository;
        this.characterRepository = characterRepository;
    }

    /**
     * 🔍 Hämta alla skills från databasen.
     */
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    /**
     * 💾 Spara en ny skill eller uppdatera en befintlig.
     */
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    /**
     * ❌ Radera en skill baserat på dess ID.
     */
    public void deleteById(Long id) {
        skillRepository.deleteById(id);
    }

    /**
     * ➕ Lägg till en befintlig skill till en specifik karaktär.
     *
     * @param characterId ID på karaktären som ska få en ny skill
     * @param skillId     ID på skillen som ska kopplas till karaktären
     */
    public void addSkillToCharacter(Long characterId, Long skillId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException(skillId));

        character.getSkills().add(skill);
        characterRepository.save(character);
    }
}