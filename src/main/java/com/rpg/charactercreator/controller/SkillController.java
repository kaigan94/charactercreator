package com.rpg.charactercreator.controller;

import com.rpg.charactercreator.model.Skill;
import com.rpg.charactercreator.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller för att hantera färdigheter (skills).
 * Base URL: /skills
 */
@RestController
@RequestMapping("/skills")
public class SkillController {

    private final SkillService skillService;

    // Konstruktor för att injicera vår service
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * 📄 GET /skills
     * Hämtar alla färdigheter som finns i systemet.
     *
     * @return Lista med alla skills
     */
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.findAll());
    }

    /**
     * ➕ POST /skills
     * Skapar en ny färdighet.
     *
     * @param skill Skill-objekt att spara
     * @return Den sparade skillen med status 201 Created
     */
    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        Skill savedSkill = skillService.save(skill);
        return ResponseEntity.status(201).body(savedSkill);
    }

    /**
     * ❌ DELETE /skills/{id}
     * Tar bort en färdighet baserat på ID.
     *
     * @param id ID för skill som ska tas bort
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 🔗 POST /skills/{characterId}/add/{skillId}
     * Lägger till en befintlig skill till en karaktär.
     *
     * @param characterId ID för karaktären
     * @param skillId ID för skillen
     * @return 200 OK
     */
    @PostMapping("/{characterId}/add/{skillId}")
    public ResponseEntity<Void> addSkillToCharacter(
            @PathVariable Long characterId,
            @PathVariable Long skillId
    ) {
        skillService.addSkillToCharacter(characterId, skillId);
        return ResponseEntity.ok().build();
    }
}