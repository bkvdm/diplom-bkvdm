package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.model.ImageUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(description = "id пользователя")
    private long id;

    @Schema(description = "Логин")
    @Size(min = 4, max = 32, message = "Login must be between 4 and 32 characters")
    private String email;

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

    @Schema(description = "Ссылка на аватар пользователя")
    private String image;
}
