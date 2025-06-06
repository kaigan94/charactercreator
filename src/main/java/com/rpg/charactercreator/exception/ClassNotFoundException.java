package com.rpg.charactercreator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ❌ ClassNotFoundException
 * Thrown when an RPG class (e.g., "Warrior", "Mage") cannot be found in the database.
 * Results in HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClassNotFoundException extends RuntimeException {

    /**
     * Konstruktor med klassnamn.
     * @param name Namnet på RPG-klassen som inte hittades
     */
    public ClassNotFoundException(String name) {
        super("Class with name '" + name + "' not found.");
    }

    /**
     * Constructor with custom message and cause.
     * @param message Custom error message
     * @param cause The underlying cause of this exception
     */
    public ClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
