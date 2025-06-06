package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 🎭 CharacterCreateDTO
 * Dataöverföringsobjekt (DTO) som används för att ta emot all nödvändig information från frontend
 * när en ny karaktär skapas i spelet.
 *
 * Detta DTO används i CharacterController vid POST /characters för att skapa en ny karaktärsprofil.
 * Innehåller detaljer såsom användar-ID som identifierar skaparen, karaktärens namn, klass,
 * bakgrundshistoria, valda färdigheter samt startföremål.
 */
@Getter
@Setter
@NoArgsConstructor
public class CharacterCreateDTO {

    /** ID för användaren som skapar karaktären. Detta är övergripande och definierar ägaren. */
    private Long userId;

    /** Namn på karaktären, vilket används för identifiering i spelet. */
    private String name;

    /** Namn på den klass som karaktären tillhör, t.ex. krigare, magiker etc. */
    private String className;

    /** Bakgrundstext eller lore som beskriver karaktärens historia och motiv. */
    private String background;

    /** Lista med valda färdighets-ID:n (måste vara exakt 3) som definierar karaktärens förmågor. */
    private List<Long> skillIds;

    /** Lista med valda startföremål (valbara av spelaren) som karaktären börjar med. */
    private List<InventoryItemDTO> startingItems;
}