package com.rpg.charactercreator.service;

import com.rpg.charactercreator.dto.ClassWithSkillsDTO;
import com.rpg.charactercreator.dto.StartingItemDTO;
import com.rpg.charactercreator.model.StartingItem;
import com.rpg.charactercreator.model.RPGClass;
import com.rpg.charactercreator.model.Skill;
import com.rpg.charactercreator.dto.SkillDTO;
import com.rpg.charactercreator.repository.RPGClassRepository;
import com.rpg.charactercreator.repository.StartingItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 🧙‍♂️ Service-klass för hantering av RPG-klasser och deras färdigheter.
 */
@Service
public class RPGClassService {
    private final RPGClassRepository classRepository;
    private final StartingItemRepository startingItemRepository;

    public RPGClassService(RPGClassRepository classRepository, StartingItemRepository startingItemRepository) {
        this.classRepository = classRepository;
        this.startingItemRepository = startingItemRepository;
    }

    private void capStats(RPGClass rpgClass) {
        rpgClass.setStrength(Math.min(rpgClass.getStrength(), 5));
        rpgClass.setDexterity(Math.min(rpgClass.getDexterity(), 5));
        rpgClass.setIntelligence(Math.min(rpgClass.getIntelligence(), 5));
        rpgClass.setConstitution(Math.min(rpgClass.getConstitution(), 5));
        rpgClass.setWisdom(Math.min(rpgClass.getWisdom(), 5));
        rpgClass.setCharisma(Math.min(rpgClass.getCharisma(), 5));
    }

    public List<RPGClass> findAll() {
        return classRepository.findAll();
    }

    public Optional<RPGClass> findByName(String name) {
        return classRepository.findByName(name);
    }

    public RPGClass createClass(RPGClass rpgClass) {
        capStats(rpgClass);
        return classRepository.save(rpgClass);
    }

    public Optional<RPGClass> findById(Long id) {
        return classRepository.findById(id);
    }

    public RPGClass save(RPGClass rpgClass) {
        capStats(rpgClass);
        return classRepository.save(rpgClass);
    }

    public List<RPGClass> saveAll(List<RPGClass> classes) {
        return classRepository.saveAll(classes);
    }

    public void deleteById(Long id) {
        classRepository.deleteById(id);
    }

    public Optional<ClassWithSkillsDTO> getClassWithSkills(String name) {
        Optional<RPGClass> optionalClass = classRepository.findByNameWithSkills(name);
        if (optionalClass.isEmpty()) {
            return Optional.empty();
        }

        RPGClass rpgClass = optionalClass.get();

        ClassWithSkillsDTO dto = new ClassWithSkillsDTO();
        dto.setName(rpgClass.getName());
        dto.setDescription(rpgClass.getDescription());
        dto.setStrength(rpgClass.getStrength());
        dto.setDexterity(rpgClass.getDexterity());
        dto.setIntelligence(rpgClass.getIntelligence());
        dto.setConstitution(rpgClass.getConstitution());
        dto.setWisdom(rpgClass.getWisdom());
        dto.setCharisma(rpgClass.getCharisma());
        dto.setRole(rpgClass.getRole());
        dto.setArmorType(rpgClass.getArmorType());
        dto.setWeapons(rpgClass.getWeapons());

        List<SkillDTO> skillDTOs = mapToSkillDTOList(rpgClass.getSkillList());
        dto.setSkills(skillDTOs);

        return Optional.of(dto);
    }

    private List<SkillDTO> mapToSkillDTOList(List<Skill> skills) {
        return skills.stream()
                .map(skill -> new SkillDTO(skill.getId(), skill.getName(), skill.getDescription()))
                .toList();
    }

    /**
     * 📦 Hämta startföremål för en given klass.
     */
    public List<StartingItemDTO> getStartingItemsForClass(String className) {
        Optional<RPGClass> rpgClassOpt = classRepository.findByName(className);

        if (rpgClassOpt.isEmpty()) {
            return List.of(); // eller kasta exception
        }

        List<StartingItem> items = startingItemRepository.findByRpgClassNameIgnoreCase(className);

        return items.stream()
                .map(item -> new StartingItemDTO(item.getName(), item.getDescription()))
                .collect(Collectors.toList());
    }
    /**
     * 🔁 Konvertera RPGClass till DTO med extra info (roll, armorType, weapons)
     */
    public ClassWithSkillsDTO toFullDTO(RPGClass rpgClass) {
        ClassWithSkillsDTO dto = new ClassWithSkillsDTO();
        dto.setName(rpgClass.getName());
        dto.setDescription(rpgClass.getDescription());
        dto.setStrength(rpgClass.getStrength());
        dto.setDexterity(rpgClass.getDexterity());
        dto.setIntelligence(rpgClass.getIntelligence());
        dto.setConstitution(rpgClass.getConstitution());
        dto.setWisdom(rpgClass.getWisdom());
        dto.setCharisma(rpgClass.getCharisma());
        dto.setRole(rpgClass.getRole());
        dto.setArmorType(rpgClass.getArmorType());
        dto.setWeapons(rpgClass.getWeapons());
        dto.setSkills(mapToSkillDTOList(rpgClass.getSkillList()));
        return dto;
    }
}
