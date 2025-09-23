package com.rpg.charactercreator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO {

    private LocalDateTime createdAt;
    private String username;
    private String email;
    private Long userId;
}
