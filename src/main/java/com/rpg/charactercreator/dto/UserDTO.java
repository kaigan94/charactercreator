package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

/**
 * 👤 UserDTO
 * Enkel DTO för att representera användare utan att exponera känslig information som lösenord.
 * Används i tex API-svar för att visa info om användare.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO {
    /**
     * Datum och tid då användaren skapades.
     */
    private LocalDateTime createdAt;

    /**
     * Användarnamn för inloggning och visning.
     */
    private String username;

    /**
     * Användarens registrerade e-postadress.
     */
    private String email;

    /**
     * Unikt ID som identifierar användaren i systemet.
     */
    private Long userId;
}
