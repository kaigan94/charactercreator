package com.rpg.charactercreator.controller;

// 📦 Importerar klasser som behövs för login och registrering
import com.rpg.charactercreator.dto.LoginRequest;
import com.rpg.charactercreator.dto.RegisterRequest;
import com.rpg.charactercreator.dto.UserDTO;
import com.rpg.charactercreator.model.User;
import com.rpg.charactercreator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 👤 AuthController
 * Ansvarar för att hantera registrering och inloggning.
 * Bas-URL: /auth
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    // 🔧 Konstruktor-injektion av vår UserService
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 📥 POST /auth/register
     * Skapar ett nytt användarkonto.
     *
     * @param request Innehåller e-post och lösenord från frontend
     * @return UserDTO utan lösenord
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.status(201).body(userService.toDTO(user));
    }

    /**
     * 🔑 POST /auth/login
     * Loggar in användare med e-post och lösenord.
     *
     * @param request Inloggningsinfo
     * @return UserDTO utan lösenord
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request);
        return ResponseEntity.ok(userService.toDTO(user));
    }
}