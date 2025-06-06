package com.rpg.charactercreator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ⚠️ GlobalExceptionHandler
 * Fångar upp och hanterar alla exception-klasser i projektet på ett enhetligt sätt.
 * Returnerar en tydlig JSON-respons till klienten med statuskod, feltyp, meddelande och timestamp.
 */
// 🔧 Detta gör att Spring fångar upp alla kastade exceptions i hela projektet och hanterar dem här.
@ControllerAdvice
public class GlobalExceptionHandler {

    // 💥 Användare finns redan
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "UserAlreadyExistsException");
    }

    // ❌ Användare hittades inte
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "UserNotFoundException");
    }

    // ❌ Karaktär hittades inte
    @ExceptionHandler(CharacterNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCharacterNotFound(CharacterNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "CharacterNotFoundException");
    }

    // ❌ Klass hittades inte
    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClassNotFound(ClassNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "ClassNotFoundException");
    }

    // 😱 Allmänt fel
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherExceptions(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Exception");
    }

    /**
     * 🧱 Gemensam metod för att skapa en strukturerad felrespons.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(Exception ex, HttpStatus status, String errorType) {

        ex.printStackTrace(); // 🪵 Logga felet i IntelliJ-terminalen

        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());
        error.put("error", errorType);
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, status);
    }
}
