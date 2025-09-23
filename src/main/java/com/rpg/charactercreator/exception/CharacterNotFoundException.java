package com.rpg.charactercreator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Undantag som kastas när en karaktär inte finns.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CharacterNotFoundException extends RuntimeException {

    /**
     * Skapar undantag med standardmeddelande för saknad karaktär.
     * @param id ID för karaktären som saknas
     */
    public CharacterNotFoundException(Long id) {
        super("Character with ID " + id + " not found.");
    }

    /**
     * Skapar undantag med eget felmeddelande.
     * @param message Eget felmeddelande
     */
    public CharacterNotFoundException(String message) {
        super(message);
    }
}
