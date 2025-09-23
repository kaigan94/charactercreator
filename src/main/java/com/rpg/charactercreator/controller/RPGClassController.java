package com.rpg.charactercreator.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rpg.charactercreator.dto.ClassWithSkillsDTO;
import com.rpg.charactercreator.dto.StartingItemDTO;
import com.rpg.charactercreator.model.RPGClass;
import com.rpg.charactercreator.service.RPGClassService;

/**
 * 🧙 Controller för RPG-klasser (bas-URL: /classes).
 * OBS: POST/PUT/DELETE skyddas av Spring Security + CSRF (se SecurityConfig).
 */
@RestController
@RequestMapping("/classes")
public class RPGClassController {

    private final RPGClassService classService;

    /** DI av service-lagret */
    public RPGClassController(RPGClassService classService) {
        this.classService = classService;
    }

    /** 📄 GET /classes – lista alla klasser (200 OK) */
    @GetMapping
    public ResponseEntity<List<RPGClass>> getAllClasses() {
        return ResponseEntity.ok(classService.findAll());
    }

    /** 🔍 GET /classes/name/{name} – hämta klass + skills via namn (200/404) */
    @GetMapping("/name/{name}")
    public ResponseEntity<ClassWithSkillsDTO> getClassByName(@PathVariable String name) {
        return classService.findByName(name)
                .map(classService::toFullDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** ➕ POST /classes – skapa ny klass (201 Created) */
    @PostMapping
    public ResponseEntity<RPGClass> createClass(@RequestBody RPGClass rpgClass) {
        return ResponseEntity.status(201).body(classService.createClass(rpgClass));
    }

    /**
     * ✏️ PUT /classes/{id} – uppdatera en klass (bara icke-null fält skrivs över).
     * Returnerar 200 OK vid lyckad uppdatering, annars 404.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RPGClass> updateClass(@PathVariable Long id, @RequestBody RPGClass updatedClass) {
        Optional<RPGClass> existingClassOpt = classService.findById(id);
        if (existingClassOpt.isEmpty()) return ResponseEntity.notFound().build();

        RPGClass existingClass = existingClassOpt.get();

        // Selektiv uppdatering (låt null betyda "lämna oförändrat")
        if (updatedClass.getName() != null) existingClass.setName(updatedClass.getName());
        if (updatedClass.getDescription() != null) existingClass.setDescription(updatedClass.getDescription());
        if (updatedClass.getArmorType() != null) existingClass.setArmorType(updatedClass.getArmorType());
        if (updatedClass.getRole() != null) existingClass.setRole(updatedClass.getRole());
        if (updatedClass.getWeapons() != null && !updatedClass.getWeapons().isEmpty()) {
            existingClass.setWeapons(updatedClass.getWeapons());
        }

        // Bas-stats (om de är primitiva kan 0 tolkas som “sätt 0”)
        existingClass.setStrength(updatedClass.getStrength());
        existingClass.setDexterity(updatedClass.getDexterity());
        existingClass.setIntelligence(updatedClass.getIntelligence());
        existingClass.setConstitution(updatedClass.getConstitution());
        existingClass.setWisdom(updatedClass.getWisdom());
        existingClass.setCharisma(updatedClass.getCharisma());

        return ResponseEntity.ok(classService.save(existingClass));
    }

    /**
     * ✏️ PUT /classes – batch-uppdatera flera klasser.
     * Null-värden skrivs inte över. Returnerar 200 OK med sparade objekt.
     */
    @PutMapping
    public ResponseEntity<List<RPGClass>> updateClasses(@RequestBody List<RPGClass> updatedClasses) {
        List<RPGClass> savedClasses = updatedClasses.stream()
                .map(updated -> {
                    Optional<RPGClass> existingOpt = classService.findById(updated.getClassId());
                    if (existingOpt.isEmpty()) return null;

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
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(savedClasses);
    }

    /** ❌ DELETE /classes/{id} – ta bort klass (204 No Content eller 404) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        Optional<RPGClass> existingClass = classService.findById(id);
        if (existingClass.isEmpty()) return ResponseEntity.notFound().build();

        classService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** 🎒 GET /classes/name/{name}/starting-items – hämta startföremål för klass (200 OK) */
    @GetMapping("/name/{name}/starting-items")
    public ResponseEntity<List<StartingItemDTO>> getStartingItemsByClassName(@PathVariable String name) {
        return ResponseEntity.ok(classService.getStartingItemsForClass(name));
    }
}