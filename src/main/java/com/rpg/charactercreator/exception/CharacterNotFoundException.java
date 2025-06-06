package com.rpg.charactercreator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 💥 Exception som kastas när en karaktär inte hittas i databasen.
 * Returnerar HTTP 404 Not Found till klienten.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CharacterNotFoundException extends RuntimeException {

    /**
     * Constructor with standard message based on ID.
     * @param id ID of the character that was not found
     */
    public CharacterNotFoundException(Long id) {
        super("Character with ID " + id + " not found.");
    }

    /**
     * Constructor with custom message.
     * @param message Custom error message
     */
    public CharacterNotFoundException(String message) {
        super(message);
    }
}
