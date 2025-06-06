package com.rpg.charactercreator.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rpg.charactercreator.exception.UserAlreadyExistsException;
import com.rpg.charactercreator.exception.UserNotFoundException;
import com.rpg.charactercreator.model.User;
import com.rpg.charactercreator.repository.UserRepository;
import com.rpg.charactercreator.dto.UserDTO;
import com.rpg.charactercreator.dto.LoginRequest;
import com.rpg.charactercreator.dto.RegisterRequest;

/**
 * Serviceklass för att hantera användarrelaterade operationer såsom registrering,
 * autentisering och hämtning av användare.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Hämtar en användare via e-postadress.
     *
     * @param email e-postadressen att söka efter
     * @return en Optional med användaren om den finns, annars tom
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Skapar en ny användare i systemet.
     *
     * @param user användarentiteten att skapa
     * @return den skapade användaren
     * @throws UserAlreadyExistsException om en användare med samma e-post redan finns
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        return userRepository.save(user);
    }

    /**
     * Hämtar en användare via unikt ID.
     *
     * @param id användarens ID att söka efter
     * @return en Optional med användaren om den finns, annars tom
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Konverterar en User-entitet till en UserDTO.
     *
     * @param user användarentiteten att konvertera
     * @return motsvarande UserDTO
     */
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    /**
     * Registrerar en ny användare baserat på registreringsförfrågan.
     *
     * @param request registreringsförfrågan med användaruppgifter
     * @return den registrerade användaren
     * @throws UserAlreadyExistsException om en användare med samma e-post redan finns
     */
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * Autentiserar en användare baserat på inloggningsförfrågan.
     *
     * @param request inloggningsförfrågan med e-post och lösenord
     * @return den autentiserade användaren
     * @throws UserNotFoundException om ingen användare med angiven e-post hittas
     * @throws RuntimeException om lösenordet är ogiltigt
     */
    public User authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No user with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    /**
     * Hämtar alla användare.
     *
     * @return en lista med alla användare
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Raderar en användare via unikt ID.
     *
     * @param id ID för användaren som ska raderas
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
