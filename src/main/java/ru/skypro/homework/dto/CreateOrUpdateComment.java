package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateComment {

    @Schema(description = "Дата и время создания комментария")
    private LocalDateTime localDateTime;

    @Schema(description = "Текст комментария")
    @Size(min = 8, max = 64, message = "The comment of the ad must be between 8 and 64 characters")
    private String text;

    @Schema(description = "id пользователя")
    private long userId;

    @Schema(description = "id объявления")
    private long adId;
}
