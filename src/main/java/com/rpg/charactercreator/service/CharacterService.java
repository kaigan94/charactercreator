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

/**
 * Affärslogik för karaktärer (skapa, uppdatera, radera, hämta).
 */
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

    /**
     * Skapar karaktär åt inloggad användare (via username).
     */
    @Transactional
    public Character createCharacterForUsername(
            CharacterCreateDTO dto,
            String username,
            String className,
            List<Long> skillIds,
            List<InventoryItemDTO> startingItems) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user with username: " + username));

        return createCharacter(dto, user.getUserId(), className, skillIds, startingItems);
    }

    /**
     * Skapar en ny karaktär: sätter klass, ägare, bas-stats, skills och start-items.
     */
    @Transactional
    public Character createCharacter(
            CharacterCreateDTO dto,
            Long userId,
            String className,
            List<Long> skillIds,
            List<InventoryItemDTO> startingItems) {

        // Hämta ägare och klass eller kasta 404/400
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        RPGClass rpgClass = classRepository.findByName(className)
                .orElseThrow(() -> new ClassNotFoundException(className));

        // Bygg entitet med defaults från klassen
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

        // Validera och sätt 3 valda skills
        validateSelectedSkills(className, skillIds);
        List<Skill> selectedSkills = skillRepository.findAllById(skillIds);
        character.setSkills(selectedSkills);

        // Spara karaktären först (behövs för foreign keys)
        Character savedCharacter = characterRepository.save(character);

        // Spara startföremål (max 3)
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

        // Lägg till klassens starting weapon i inventory om den finns
        String startingWeapon = rpgClass.getStartingWeapon();
        if (startingWeapon != null && !startingWeapon.isBlank()) {
            inventoryItemRepository.save(new InventoryItem(startingWeapon, "Class starting weapon.", savedCharacter));
        }

        // Lägg på standardutrustning (t.ex. rustning) baserat på armor-type/roll
        assignDefaultEquipment(savedCharacter, rpgClass.getRole());

        return savedCharacter;
    }

    /**
     * Hämta alla karaktärer (utan pagination).
     */
    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }

    /**
     * Sök karaktärer på namn (case-insensitive).
     */
    public List<Character> searchByName(String name) {
        return characterRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Hämta alla karaktärer + mappa till DTO.
     */
    public List<CharacterWithDetailsDTO> getAllCharactersWithDetails() {
        return characterRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapper: Character -> CharacterWithDetailsDTO (gömmer känsligt & plattar relationer).
     */
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

    /**
     * Sök + returnera som DTO-lista.
     */
    public List<CharacterWithDetailsDTO> searchByNameWithDetails(String name) {
        return characterRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Hämta alla karaktärer med pagination som DTO.
     */
    public Page<CharacterWithDetailsDTO> getAllCharactersWithDetails(Pageable pageable) {
        return characterRepository.findAll(pageable).map(this::toDTO);
    }

    /**
     * Ägarkoll: stämmer username med karaktärens ägare?
     */
    @Transactional(readOnly = true)
    public boolean isOwner(Long characterId, String username) {
        Character c = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));
        return c.getUser() != null && username != null && username.equals(c.getUser().getUsername());
    }

    /**
     * Partiell uppdatering av stats/namn/klass/skills.
     */
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
     * Hämta karaktär eller kasta 404.
     */
    @Transactional(readOnly = true)
    public Character getById(Long id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
    }

    /**
     * Ta bort karaktär (används av controller efter ägarkoll/rollkoll).
     */
    public void deleteById(Long id) {
        characterRepository.deleteById(id);
    }

    /**
     * Validerar att exakt 3 skills valts och att de hör till klassen.
     */
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

    /**
     * Lägg till ett inventory-item till en karaktär.
     */
    @Transactional
    public void addInventoryItem(Long characterId, InventoryItemDTO dto) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId));

        InventoryItem item = new InventoryItem(dto.getName(), dto.getDescription(), character);
        inventoryItemRepository.save(item);
    }

    /**
     * Hämta en karaktärs inventory som DTO-lista (namn + beskrivning).
     */
    @Transactional(readOnly = true)
    public List<InventoryItemDTO> getInventoryForCharacter(Long characterId) {
        List<InventoryItem> items = inventoryItemRepository.findByCharacterId(characterId);

        return items.stream()
                .map(item -> new InventoryItemDTO(item.getName(), item.getDescription()))
                .collect(Collectors.toList());
    }

    /**
     * Standardutrustning baserat på roll och armor-type.
     */
    private void assignDefaultEquipment(Character character, String role) {
        if (role == null) return;

        RPGClass rpgClass = character.getRpgClass();
        if (rpgClass != null && rpgClass.getArmorType() != null) {
            String armor = switch (rpgClass.getArmorType().toLowerCase()) {
                case "cloth" -> "Traveler's Cloth Robe";
                case "leather" -> "Traveler's Leather Vest";
                case "plate" -> "Traveler's Armor";
                default -> "Traveler's Clothes";
            };
            inventoryItemRepository.save(new InventoryItem(armor, "Basic armor for your class.", character));
        }
    }
    

    /**
     * Hämta alla karaktärer för en given userId.
     */
    public List<Character> getCharactersByUserId(Long userId) {
        return characterRepository.findAll()
                .stream()
                .filter(c -> c.getUser() != null && c.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}