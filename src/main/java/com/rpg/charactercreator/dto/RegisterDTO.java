package com.rpg.charactercreator.dto;

import jakarta.validation.constraints.*;

public class RegisterDTO {
    @NotBlank @Size(min=3, max=32)
    private String username;

    @NotBlank @Size(min=8, max=72)
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d).+$",
            message="Must contain at least one letter and one number.")
    private String password;

    @NotBlank
    private String confirmPassword;

    // getters/setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}