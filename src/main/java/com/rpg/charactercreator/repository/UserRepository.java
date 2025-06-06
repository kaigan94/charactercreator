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
     *
     * @param email e-postadress att söka efter
     * @return Optional med användare om den finns
     */
    Optional<User> findByEmail(String email);

    /**
     * ✅ Kontrollera om en användare med en viss e-postadress redan finns.
     *
     * @param email e-postadress att kontrollera
     * @return true om e-postadressen finns, annars false
     */
    boolean existsByEmail(String email);

    /**
     * 🧮 Hämta användare som har fler än ett visst antal karaktärer.
     * Använder native SQL och subquery för att räkna karaktärer per användare.
     *
     * @param count Antal karaktärer som tröskel
     * @return Lista över användare med fler än {count} karaktärer
     */
    @Query(
            value = """
            SELECT * FROM user
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
