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

    /**
     * 🔍 Hämta alla RPG-klasser.
     *
     * @return Lista med alla RPG-klasser
     */
    public List<RPGClass> findAll() {
        return classRepository.findAll();
    }

    /**
     * 🔍 Hitta RPG-klass baserat på namn.
     *
     * @param name Klassens namn
     * @return Optional med RPGClass (eller tomt om ingen hittades)
     */
    public Optional<RPGClass> findByName(String name) {
        return classRepository.findByName(name);
    }

    /**
     * ➕ Skapa en ny RPG-klass.
     *
     * @param rpgClass Den RPG-klass som ska skapas
     * @return Den skapade RPG-klassen
     */
    public RPGClass createClass(RPGClass rpgClass) {
        capStats(rpgClass);
        return classRepository.save(rpgClass);
    }

    /**
     * 🔍 Hitta klass via ID.
     *
     * @param id Klassens ID
     * @return Optional med RPGClass (eller tomt om ingen hittades)
     */
    public Optional<RPGClass> findById(Long id) {
        return classRepository.findById(id);
    }

    /**
     * 💾 Spara eller uppdatera en klass.
     *
     * @param rpgClass Den RPG-klass som ska sparas eller uppdateras
     * @return Den sparade eller uppdaterade RPG-klassen
     */
    public RPGClass save(RPGClass rpgClass) {
        capStats(rpgClass);
        return classRepository.save(rpgClass);
    }

    /**
     * 💾 Spara eller uppdatera flera klasser samtidigt.
     *
     * @param classes Lista med RPG-klasser som ska sparas eller uppdateras
     * @return Lista med sparade eller uppdaterade RPG-klasser
     */
    public List<RPGClass> saveAll(List<RPGClass> classes) {
        return classRepository.saveAll(classes);
    }

    /**
     * ❌ Radera en klass via ID.
     *
     * @param id Klassens ID som ska raderas
     */
    public void deleteById(Long id) {
        classRepository.deleteById(id);
    }

    /**
     * 📦 Hämta klass med tillhörande färdigheter som DTO.
     *
     * @param name Namnet på RPG-klassen
     * @return Optional med DTO innehållande klassdata och färdigheter
     */
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
     *
     * @param className Namnet på RPG-klassen
     * @return Lista med startföremål som DTO:er
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
     *
     * @param rpgClass RPGClass-entiteten
     * @return ClassWithSkillsDTO med roll, armor och vapen
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
