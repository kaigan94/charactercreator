package com.rpg.charactercreator.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.rpg.charactercreator.exception.UserAlreadyExistsException;
import com.rpg.charactercreator.exception.UserNotFoundException;
import com.rpg.charactercreator.model.User;
import com.rpg.charactercreator.repository.UserRepository;
import com.rpg.charactercreator.repository.RoleRepository;
import com.rpg.charactercreator.model.Role;
import com.rpg.charactercreator.dto.UserDTO;
import com.rpg.charactercreator.dto.RegisterRequest;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    @Transactional
    public User register(RegisterRequest request) {
        // normalisera/validera
        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();
        String rawPassword = request.getPassword() == null ? "" : request.getPassword();

        if (username.isEmpty() || email.isEmpty() || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Username, email and password are required.");
        }
        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);

        // säkerställ att ROLE_USER finns och koppla
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}