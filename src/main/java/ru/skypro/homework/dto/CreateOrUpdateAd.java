package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "The ad price must be greater than or equal to 0")
    @Max(value = 1000000, message = "The ad price must be less than or equal to 1 000 000")
    private int price;

    @Schema(description = "Описание объявления")
    @Size(min = 8, max = 64, message = "The description of the ad must be between 8 and 64 characters")
    private String description;

    @Schema(description = "id пользователя")
    private Long userId;

    @Schema(description = "id картинки объявления")
    private Long imageAdId;
}
