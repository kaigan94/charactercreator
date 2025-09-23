# 📚 Hemtenta - Spring Security

### Hot 1: SQL Injection

#### 1.1 Hotet och implementation
- SQL injection = någon försöker köra egen SQL via API-input.
- Mitt projekt använder Spring Data JPA/ Hibernate vilket motverkar SQLi genom parameterisering.
- Vi använder endast metoder definierade i repositories (inga dynamiska strängkonkatenerade queries). Eventuella native queries använder bindparametrar.

### Hot 2: Trasig autentisering
#### 2.1 Hotet och lösenordssäkerhet
- Vanliga problem: lösenord i plain text, svaga hash, brute force
- Mitt projekt använder BCrypt för lösenord 👇

```java
@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

#### 2.2 Session-baserad autentisering
- Vid login får användaren en JSESSIONID-cookie
- Servern håller sessionen tills man loggar ut eller sessionen expires

### Hot 3: Trasig auktorisering
#### 3.1 Hotet och rollbaserad säkerhet
- Privilege escalation = vanlig användare försöker nå admin-endpoint
- Skydd i min SecurityConfig 👇

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/characters/**").authenticated()
)
```

#### 3.2 HTTP-statuskoder
- 401 Unauthorized = ej inloggad

- 403 Forbidden = inloggad men saknar rätt role
T.ex: 403 om en användare försöker ta bort någon annans karaktär

### Hot 4: Exponering av känslig data
#### 4.1 Dataskydd
- Känslig data = lösenord, email, interna ID
- Jag använder DTOs för att inte råka skicka iväg för mycket 👇

```java
public class CharacterWithDetailsDTO {
    private String name;
    private int level;
    private String className;
    // inga lösenord eller interna user-objekt skickas
}
```

### Hot 5: Säkerhetskonfigurationsfel
#### 5.1 Spring Security konfiguration
- I min SecurityConfig bestämmer jag öppna/skyddade endpoints 👇

- Öppet: /auth/**, /login, /logout, static resources

- Skyddat: /characters/**, /admin/**

```java
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

.authorizeHttpRequests(auth -> auth
    .requestMatchers("/csrf-token", "/auth/**", "/login", "/logout").permitAll()
    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // static
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/characters/**").authenticated()
    .anyRequest().authenticated()
)
```