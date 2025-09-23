package com.rpg.charactercreator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Denna klass används vid inloggning för att ta emot användarens inloggningsuppgifter.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // Användarens e-postadress
    private String email;

    // Användarens lösenord
    private String password;
}