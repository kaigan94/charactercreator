package com.rpg.charactercreator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ❌ SkillNotFoundException
 * Egen exception som kastas när en skill inte finns i databasen.
 * Returnerar automatiskt HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SkillNotFoundException extends RuntimeException {

    // Konstruktor för att skapa felmeddelande med skill-ID
    public SkillNotFoundException(Long id) {
        super("Skill with ID " + id + " not found.");
    }

    // Konstruktor för att skapa felmeddelande med egen text
    public SkillNotFoundException(String message) {
        super(message);
    }
}