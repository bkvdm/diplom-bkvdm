package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateAd {

    @Schema(description = "Заголовок объявления")
    @Size(min = 4, max = 32, message = "The title of the ad must be between 4 and 32 characters")
    private String title;

    @Schema(description = "Цена объявления")
    @Size(min = 0, max = 1000000, message = "The ad price must be between 0 and 1 000 000 values")
    private int price;

    @Schema(description = "Описание объявления")
    @Size(min = 8, max = 64, message = "The description of the ad must be between 8 and 64 characters")
    private String description;

    @Schema(description = "id пользователя")
    private Long userId;

    @Schema(description = "id картинки объявления")
    private Long imageAdId;
}
