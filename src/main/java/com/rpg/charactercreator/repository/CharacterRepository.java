package com.rpg.charactercreator.repository;

import com.rpg.charactercreator.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    List<Character> findByNameContainingIgnoreCase(String name);
}