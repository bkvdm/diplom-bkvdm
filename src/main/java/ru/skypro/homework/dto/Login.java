package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {

    @Schema(description = "Логин")
    @Size(min = 4, max = 32, message = "Login must be between 4 and 32 characters")
    private String username;

    @Schema(description = "Пароль")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;
}
