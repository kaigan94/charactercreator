package com.rpg.charactercreator.model;

import com.rpg.charactercreator.dto.CharacterCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 📦 Wrapper för att skapa en karaktär med tillhörande info.
 * Innehåller CharacterCreateDTO + userId + className + valda skillIds.
 * <p>
 * Denna klass kan användas som hjälpklass för frontend-kommunikation,
 * t.ex. i alternativa endpoints där det behövs ett samlat request-objekt.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterRequest {
    /**
     * DTO med karaktärsdata som ska skapas.
     */
    private CharacterCreateDTO character;

    /**
     * ID för användaren som skapar karaktären.
     */
    private Long userId;

    /**
     * Namn på den valda klassen för karaktären.
     */
    private String className;

    /**
     * Lista med valda skill-ID:n som karaktären ska ha.
     */
    private List<Long> skillIds;
}