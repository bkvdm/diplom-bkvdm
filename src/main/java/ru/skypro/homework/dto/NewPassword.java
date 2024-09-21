package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewPassword {

    @Schema(description = "Старый пароль")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String currentPassword;

    @Schema(description = "Новый пароль")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String newPassword;
}
