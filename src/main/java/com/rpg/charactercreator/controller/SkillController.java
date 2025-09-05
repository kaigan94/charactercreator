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

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * 📄 GET /skills
     * Hämtar alla färdigheter som finns i systemet.
     */
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.findAll());
    }

    /**
     * ➕ POST /skills
     * Skapar en ny skill.
     */
    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        Skill savedSkill = skillService.save(skill);
        return ResponseEntity.status(201).body(savedSkill);
    }

    /**
     * ❌ DELETE /skills/{id}
     * Tar bort en färdighet baserat på ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 🔗 POST /skills/{characterId}/add/{skillId}
     * Lägger till en befintlig skill till en karaktär.
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