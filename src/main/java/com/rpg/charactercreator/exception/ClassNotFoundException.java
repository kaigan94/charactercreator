package com.rpg.charactercreator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ❌ ClassNotFoundException
 * Kastas när en RPG-klass (t.ex. "Warrior", "Mage") inte hittas i databasen.
 * Returnerar HTTP 404 Not Found till klienten.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClassNotFoundException extends RuntimeException {

    /**
     * Konstruktor som används när en klass inte hittas baserat på namn.
     * @param name Namnet på klassen som saknas
     */
    public ClassNotFoundException(String name) {
        super("Class with name '" + name + "' not found.");
    }

    /**
     * Konstruktor som låter oss skicka in ett eget felmeddelande och en orsak.
     * @param message Anpassat felmeddelande
     * @param cause Orsaken till felet (annan exception)
     */
    public ClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}