package com.rpg.charactercreator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO för registrering av användare.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // Användarnamn för den nya användaren
    private String username;

    // E-postadress för den nya användaren
    private String email;

    // Lösenord för den nya användaren
    private String password;
}