package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class Register {

    @Schema(description = "Логин")
    @Size(min = 4, max = 32, message = "Login must be between 4 and 32 characters")
    private String username;

    @Schema(description = "Пароль")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;

    @Schema(description = "Имя пользователя")
    @Size(min = 2, max = 16, message = "First name must be between 2 and 16 characters")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    @Size(min = 2, max = 16, message = "Last name must be between 2 and 16 characters")
    private String lastName;

    @Schema(description = "Телефон пользователя")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "The phone must follow the pattern +7 (XXX) XXX-XX-XX")
    private String phone;

    @Schema(description = "Роль пользователя")
    private Role role;
}
