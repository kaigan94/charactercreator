package com.rpg.charactercreator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ❌ SkillNotFoundException
 * Kastas när en skill inte hittas i databasen.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException(Long id) {
        super("Skill with ID " + id + " not found.");
    }

    public SkillNotFoundException(String message) {
        super(message);
    }
}