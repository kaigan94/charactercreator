package com.rpg.charactercreator.exception;

import java.io.Serial;

/**
 * ❌ Kastas när en användare med angivet ID inte hittas i databasen.
 * Används i service-lagret för att signalera att användaren inte finns.
 */
public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Skapar ett undantag med ett felmeddelande som inkluderar användarens ID.
     *
     * @param id ID:t för användaren som inte kunde hittas
     */
    public UserNotFoundException(Long id) {
        super("User with ID " + id + " not found.");
    }

    /**
     * Skapar ett undantag med ett eget meddelande.
     *
     * @param message Anpassat felmeddelande
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
