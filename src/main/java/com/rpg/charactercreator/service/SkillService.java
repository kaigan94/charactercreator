package com.rpg.charactercreator.service;

import com.rpg.charactercreator.exception.CharacterNotFoundException;
import com.rpg.charactercreator.exception.SkillNotFoundException;
import com.rpg.charactercreator.model.Character;
import com.rpg.charactercreator.model.Skill;
import com.rpg.charactercreator.repository.CharacterRepository;
import com.rpg.charactercreator.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;

    public SkillService(SkillRepository skillRepository, CharacterRepository characterRepository) {
        this.skillRepository = skillRepository;
        this.characterRepository = characterRepository;
    }

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    public void deleteById(Long id) {
        skillRepository.deleteById(id);
    }

    /**
     * Lägg till en befintlig skill till en specifik karaktär.
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