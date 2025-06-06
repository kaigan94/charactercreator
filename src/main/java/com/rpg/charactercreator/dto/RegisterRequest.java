package com.rpg.charactercreator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📥 RegisterRequest
 * DTO som innehåller användarens registreringsdata:
 * användarnamn, email och lösenord.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    /**
     * Användarnamn som användaren vill registrera sig med.
     */
    private String username;

    /**
     * E-postadress för att skapa kontot.
     */
    private String email;

    /**
     * Lösenord som ska användas för kontot.
     */
    private String password;
}