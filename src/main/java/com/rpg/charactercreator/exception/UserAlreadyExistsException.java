package com.rpg.charactercreator.exception;

import java.io.Serial;

/**
 * 🚫 Undantag som kastas när man försöker skapa en användare med en e-post som redan finns i databasen.
 * Används i UserService för att stoppa dubblett-registreringar.
 */
public class UserAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Skapar en ny UserAlreadyExistsException med ett meddelande som innehåller den aktuella e-postadressen.
     *
     * @param email e-postadressen som redan är registrerad
     */
    public UserAlreadyExistsException(String email) {
        super("User with email " + email + " already exists.");
    }
}
