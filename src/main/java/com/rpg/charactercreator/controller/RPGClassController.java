package com.rpg.charactercreator.controller;

import java.util.List;
import java.util.Optional;

import com.rpg.charactercreator.dto.SkillDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rpg.charactercreator.dto.ClassWithSkillsDTO;
import com.rpg.charactercreator.dto.StartingItemDTO;
import com.rpg.charactercreator.model.RPGClass;
import com.rpg.charactercreator.service.RPGClassService;

/**
 * 🧙 Controller för att hantera RPG-klasser.
 * Bas-URL: /classes
 */
@RestController
@RequestMapping("/classes")
public class RPGClassController {

    private final RPGClassService classService;

    /**
     * Konstruktor för RPGClassController.
     * @param classService Service för RPG-klasser
     */
    public RPGClassController(RPGClassService classService) {
        this.classService = classService;
    }

    /**
     * 📄 GET /classes
     * Hämtar alla RPG-klasser i systemet
     */
    @GetMapping
    public ResponseEntity<List<RPGClass>> getAllClasses() {
        return ResponseEntity.ok(classService.findAll());
    }


    /**
     * 🔍 GET /classes/name/{name}
     * Hämta en klass baserat på dess namn
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ClassWithSkillsDTO> getClassByName(@PathVariable String name) {
        return classService.findByName(name)
            .map(classService::toFullDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    /**
     * ➕ POST /classes
     * Skapa en ny RPG-klass med namn och beskrivning
     */
    @PostMapping
    public ResponseEntity<RPGClass> createClass(@RequestBody RPGClass rpgClass) {
        return ResponseEntity.status(201).body(classService.createClass(rpgClass));
    }


    /**
     * ✏️ PUT /classes/{id}
     * Uppdatera en befintlig klass med ny data.
     * Endast icke-null värden uppdateras
     */
    @PutMapping("/{id}")
    public ResponseEntity<RPGClass> updateClass(@PathVariable Long id, @RequestBody RPGClass updatedClass) {
        Optional<RPGClass> existingClassOpt = classService.findById(id);

        if (existingClassOpt.isPresent()) {
            RPGClass existingClass = existingClassOpt.get();

            if (updatedClass.getName() != null) existingClass.setName(updatedClass.getName());
            if (updatedClass.getDescription() != null) existingClass.setDescription(updatedClass.getDescription());
            if (updatedClass.getArmorType() != null) existingClass.setArmorType(updatedClass.getArmorType());
            if (updatedClass.getRole() != null) existingClass.setRole(updatedClass.getRole());

            if (updatedClass.getWeapons() != null && !updatedClass.getWeapons().isEmpty()) {
                existingClass.setWeapons(updatedClass.getWeapons());
            }

            existingClass.setStrength(updatedClass.getStrength());
            existingClass.setDexterity(updatedClass.getDexterity());
            existingClass.setIntelligence(updatedClass.getIntelligence());
            existingClass.setConstitution(updatedClass.getConstitution());
            existingClass.setWisdom(updatedClass.getWisdom());
            existingClass.setCharisma(updatedClass.getCharisma());

            RPGClass saved = classService.save(existingClass);
            return ResponseEntity.ok(saved);

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * ✏️ PUT /classes
     * Uppdatera flera RPG-klasser samtidigt.
     * Null-värden skrivs inte över.
     */
    @PutMapping
    public ResponseEntity<List<RPGClass>> updateClasses(@RequestBody List<RPGClass> updatedClasses) {

        List<RPGClass> savedClasses = updatedClasses.stream()
                .map(updated -> {
                    Optional<RPGClass> existingOpt = classService.findById(updated.getClassId());

                    if (existingOpt.isPresent()) {
                        RPGClass existing = existingOpt.get();

                        if (updated.getName() != null) existing.setName(updated.getName());
                        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
                        if (updated.getArmorType() != null) existing.setArmorType(updated.getArmorType());
                        if (updated.getRole() != null) existing.setRole(updated.getRole());

                        if (updated.getWeapons() != null && !updated.getWeapons().isEmpty()) {
                            existing.setWeapons(updated.getWeapons());
                        }

                        existing.setStrength(updated.getStrength());
                        existing.setDexterity(updated.getDexterity());
                        existing.setIntelligence(updated.getIntelligence());
                        existing.setConstitution(updated.getConstitution());
                        existing.setWisdom(updated.getWisdom());
                        existing.setCharisma(updated.getCharisma());

                        return classService.save(existing);
                    }

                    return null;
                })
                .filter(c -> c != null)
                .toList();

        return ResponseEntity.ok(savedClasses);
    }


    /**
     * ❌ DELETE /classes/{id}
     * Ta bort en RPG-klass baserat på ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        Optional<RPGClass> existingClass = classService.findById(id);

        if (existingClass.isPresent()) {
            classService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * 🎒 GET /classes/name/{name}/starting-items
     * Hämta startföremål som hör till en viss klass.
     */
    @GetMapping("/name/{name}/starting-items")
    public ResponseEntity<List<StartingItemDTO>> getStartingItemsByClassName(@PathVariable String name) {
        List<StartingItemDTO> items = classService.getStartingItemsForClass(name);
        return ResponseEntity.ok(items);
    }

}