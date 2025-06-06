package com.rpg.charactercreator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 👤 User
 * Representerar en användare i systemet. En användare kan skapa karaktärer.
 */
@Entity // 🔗 Markerar denna klass som en JPA-entity (kopplas till en databas-tabell)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    /** 🆔 Primärnyckel – auto-genererad ID för varje användare */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /** 📛 Användarnamn */
    @Column(nullable = false)
    private String username;

    /** 📧 E-postadress */
    @Column(nullable = false)
    private String email;

    /** 🔒 Lösenord (ska inte skickas till frontend!) */
    @Column(nullable = false)
    private String password;

    /** 🕒 Registreringsdatum (sätts automatiskt) */
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 🧾 toString
     * Returnerar en text-representation av användaren, t.ex. för debugging/loggning.
     */
    @Override
    public String toString() {
        return "User{\n" +
                "  userId=" + userId + ",\n" +
                "  username='" + username + "',\n" +
                "  email='" + email + "',\n" +
                "  password='" + password + "',\n" +
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
