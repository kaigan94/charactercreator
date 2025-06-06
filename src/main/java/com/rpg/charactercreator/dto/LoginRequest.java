package com.rpg.charactercreator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 🔐 LoginRequest
 * DTO som används vid inloggning. Innehåller email och lösenord.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /** Användarens email */
    private String email;

    /** Användarens lösen */
    private String password;
}