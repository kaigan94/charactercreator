package com.rpg.charactercreator.repository;

import com.rpg.charactercreator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 🧑‍💼 UserRepository
 * Interface för att hantera databasoperationer relaterade till User-entiteten.
 * Använder Spring Data JPA för att förenkla CRUD och custom queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 🔍 Hitta användare baserat på e-postadress (case-insensitive).
     */
    Optional<User> findByEmail(String email);

    /**
     * 🔍 Hitta användare baserat på användarnamn (case-sensitive enligt DB-collation).
     */
    Optional<User> findByUsername(String username);

    /**
     * ✅ Kontrollera om ett användarnamn redan finns (för registrering).
     */
    boolean existsByUsername(String username);

    /**
     * ✅ Kontrollera om en användare med en viss e-postadress redan finns.
     */
    boolean existsByEmail(String email);

    /**
     * 🧮 Hämta användare som har fler än ett visst antal karaktärer.
     * Använder native SQL och subquery för att räkna karaktärer per användare.
     */
    @Query(
            value = """
            SELECT * FROM users
            WHERE user_id IN (
                SELECT user_id
                FROM characters
                GROUP BY user_id
                HAVING COUNT(*) > :count
            )
            """,
            nativeQuery = true
    )
    List<User> findUsersWithMoreThanXCharacters(@Param("count") int count);
}
