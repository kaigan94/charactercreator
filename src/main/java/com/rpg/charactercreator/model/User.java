package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

/**
 * User
 * Representerar en användare i systemet. En user kan skapa karaktärer.
 */
@Entity //  Markerar denna klass som en JPA-entity (kopplas till en databas-tabell)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    /** Primary key – auto generated ID för varje användare */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    /** password (ska inte skickas till frontend!!!) */
    @Column(nullable = false)
    private String password;
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "User{\n" +
                "  userId=" + userId + ",\n" +
                "  username='" + username + "',\n" +
                "  email='" + email + "',\n" +
                "  enabled=" + enabled + ",\n" +
                "  createdAt=" + createdAt + "\n" +
                '}';
    }

    /**
     * 🔁 equals
     * Används för att jämföra två User-objekt baserat på deras ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId, user.userId);
    }

    /**
     * 🔁 hashCode
     * Returnerar ett unikt hash-värde baserat på userId – behövs för hash-baserade collection-klasser.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
