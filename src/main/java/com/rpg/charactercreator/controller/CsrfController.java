package com.rpg.charactercreator.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller för att hämta CSRF-token.
 * Gör det möjligt för frontend att läsa ut token som behövs för POST/PUT/DELETE.
 */
@RestController
public class CsrfController {

    /**
     * Endpoint som returnerar aktuell CSRF-token i JSON-format.
     * Exempel: { "token": "abc123..." }
     */
    @GetMapping("/csrf-token")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("token", token.getToken());
    }
}