package com.rpg.charactercreator.service;

import com.rpg.charactercreator.dto.CharacterCreateDTO;
import com.rpg.charactercreator.dto.CharacterUpdateDTO;
import com.rpg.charactercreator.dto.CharacterWithDetailsDTO;
import com.rpg.charactercreator.dto.InventoryItemDTO;
import com.rpg.charactercreator.exception.CharacterNotFoundException;
import com.rpg.charactercreator.exception.ClassNotFoundException;
import com.rpg.charactercreator.exception.UserNotFoundException;
import com.rpg.charactercreator.model.Character;
import com.rpg.charactercreator.model.InventoryItem;
import com.rpg.charactercreator.model.RPGClass;
import com.rpg.charactercreator.model.Skill;
import com.rpg.charactercreator.model.User;
import com.rpg.charactercreator.repository.CharacterRepository;
import com.rpg.charactercreator.repository.InventoryItemRepository;
import com.rpg.charactercreator.repository.RPGClassRepository;
import com.rpg.charactercreator.repository.SkillRepository;
import com.rpg.charactercreator.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final RPGClassRepository classRepository;
    private final SkillRepository skillRepository;
    private final InventoryItemRepository inventoryItemRepository;


    public CharacterService(
            CharacterRepository characterRepository,
            UserRepository userRepository,
            RPGClassRepository classRepository,
            SkillRepository skillRepository,
            InventoryItemRepository inventoryItemRepository) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.skillRepository = skillRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Transactional
    public Character createCharacter(
            CharacterCreateDTO dto,
            Long userId,
            String className,
            List<Long> skillIds,
            List<InventoryItemDTO> startingItems) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        RPGClass rpgClass = classRepository.findByName(className)
                .orElseThrow(() -> new ClassNotFoundException(className));

        Character character = new Character();
        character.setName(dto.getName());
        character.setLevel(1);
        character.setUser(user);
        character.setBackground(dto.getBackground());
        character.setRpgClass(rpgClass);
        character.setStrength(rpgClass.getStrength());
        character.setDexterity(rpgClass.getDexterity());
        character.setIntelligence(rpgClass.getIntelligence());
        character.setConstitution(rpgClass.getConstitution());
        character.setWisdom(rpgClass.getWisdom());
        character.setCharisma(rpgClass.getCharisma());

        validateSelectedSkills(className, skillIds);

        List<Skill> selectedSkills = skillRepository.findAllById(skillIds);
        character.setSkills(selectedSkills);

        Character savedCharacter = characterRepository.save(character);

        if (startingItems != null && startingItems.size() > 3) {
            throw new IllegalArgumentException("You can only select up to 3 starting items.");
        }
        if (startingItems != null) {
            List<InventoryItem> selectedItems = startingItems.stream()
                .filter(item -> item.getName() != null && item.getDescription() != null)
                .map(item -> new InventoryItem(item.getName(), item.getDescription(), savedCharacter))
                .collect(Collectors.toList());
            inventoryItemRepository.saveAll(selectedItems);
        }

        assignDefaultEquipment(savedCharacter, rpgClass.getRole());

        return savedCharacter;
    }


    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }


    public List<Character> searchByName(String name) {
        return characterRepository.findByNameContainingIgnoreCase(name);
    }


    public List<CharacterWithDetailsDTO> getAllCharactersWithDetails() {
        return characterRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public CharacterWithDetailsDTO toDTO(Character character) {
        CharacterWithDetailsDTO dto = new CharacterWithDetailsDTO();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setLevel(character.getLevel());
        dto.setBackground(character.getBackground());
        dto.setSkills(character.getSkills().stream().map(Skill::getName).collect(Collectors.toList()));
        dto.setClassName(character.getRpgClass().getName());
        dto.setRole(character.getRpgClass().getRole());
        dto.setArmorType(character.getRpgClass().getArmorType());
        dto.setWeapons(character.getRpgClass().getWeapons());
        dto.setUsername(character.getUser() != null ? character.getUser().getUsername() : "unknown");
        dto.setStrength(character.getStrength());
        dto.setDexterity(character.getDexterity());
        dto.setIntelligence(character.getIntelligence());
        dto.setConstitution(character.getConstitution());
        dto.setWisdom(character.getWisdom());
        dto.setCharisma(character.getCharisma());
        dto.setInventory(character.getInventoryItems().stream()
                .map(InventoryItem::getName)
                .collect(Collectors.toList()));
        return dto;
    }


    public List<CharacterWithDetailsDTO> searchByNameWithDetails(String name) {
        return characterRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public Page<CharacterWithDetailsDTO> getAllCharactersWithDetails(Pageable pageable) {
        return characterRepository.findAll(pageable).map(this::toDTO);
    }


    @Transactional
    public Character updateCharacter(Long id, CharacterUpdateDTO updateDTO) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));

        if (updateDTO.getStrength() != null) character.setStrength(updateDTO.getStrength());
        if (updateDTO.getDexterity() != null) character.setDexterity(updateDTO.getDexterity());
        if (updateDTO.getIntelligence() != null) character.setIntelligence(updateDTO.getIntelligence());
        if (updateDTO.getConstitution() != null) character.setConstitution(updateDTO.getConstitution());
        if (updateDTO.getWisdom() != null) character.setWisdom(updateDTO.getWisdom());
        if (updateDTO.getCharisma() != null) character.setCharisma(updateDTO.getCharisma());
        if (updateDTO.getName() != null) character.setName(updateDTO.getName());
        if (updateDTO.getClassName() != null) {
            RPGClass newClass = classRepository.findByName(updateDTO.getClassName())
                    .orElseThrow(() -> new ClassNotFoundException(updateDTO.getClassName()));
            character.setRpgClass(newClass);
        }
        if (updateDTO.getSkillIds() != null) {
            List<Skill> updatedSkills = skillRepository.findAllById(updateDTO.getSkillIds());
            character.getSkills().clear();
            character.getSkills().addAll(updatedSkills);
        }

        return characterRepository.save(character);
    }


    /**
     * Deletes a character by its id.
     * @param id the id of the character to delete
     */
    public void deleteById(Long id) {
        characterRepository.deleteById(id);
    }


    private void validateSelectedSkills(String className, List<Long> skillIds) {
        if (skillIds == null || skillIds.size() != 3)
            throw new IllegalArgumentException("You must select 3 starting skills.");

        RPGClass rpgClass = classRepository.findByName(className)
                .orElseThrow(() -> new ClassNotFoundException(className));

        List<Long> allowedSkillIds = rpgClass.getSkillList()
                .stream()
                .map(Skill::getId)
                .toList();

        for (Long id : skillIds) {
            if (!allowedSkillIds.contains(id)) {
                throw new IllegalArgumentException("Skill ID " + id + " is not allowed for class " + className);
            }
        }
    }


    @Transactional
    public void addInventoryItem(Long characterId, InventoryItemDTO dto) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));

        InventoryItem item = new InventoryItem(dto.getName(), dto.getDescription(), character);
        inventoryItemRepository.save(item);
    }


    @Transactional(readOnly = true)
    public List<InventoryItemDTO> getInventoryForCharacter(Long characterId) {
        List<InventoryItem> items = inventoryItemRepository.findByCharacterId(characterId);

        return items.stream()
                .map(item -> new InventoryItemDTO(item.getName(), item.getDescription()))
                .collect(Collectors.toList());
    }


    private void assignDefaultEquipment(Character character, String role) {
        if (role == null) return;

        role = role.toLowerCase();
        if (role.contains("melee damage")) {
            inventoryItemRepository.save(new InventoryItem("Iron Sword", "A reliable blade for melee combat.", character));
        }
        if (role.contains("ranged damage")) {
            inventoryItemRepository.save(new InventoryItem("Hunting Bow", "A basic but sturdy bow for ranged attacks.", character));
        }
        if (role.contains("healer")) {
            inventoryItemRepository.save(new InventoryItem("Healer's Staff", "Focuses healing magic.", character));
        }
        if (role.contains("tank")) {
            inventoryItemRepository.save(new InventoryItem("Wooden Shield", "Provides basic protection.", character));
        }

        RPGClass rpgClass = character.getRpgClass();
        if (rpgClass != null && rpgClass.getArmorType() != null) {
            String armor = switch (rpgClass.getArmorType().toLowerCase()) {
                case "cloth" -> "Cloth Robe";
                case "leather" -> "Leather Vest";
                case "plate" -> "Iron Plate Armor";
                default -> "Traveler's Clothes";
            };
            inventoryItemRepository.save(new InventoryItem(armor, "Basic armor for your class.", character));
        }
    }


    /**
     * Returns a list of characters belonging to a specific user.
     * @param userId the user id
     * @return list of characters
     */
    public List<Character> getCharactersByUserId(Long userId) {
        return characterRepository.findAll()
                .stream()
                .filter(c -> c.getUser() != null && c.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

}