package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AdReview {

    private int count;
    private List<AdResult> results;

    @Data
    public static class AdResult {

        @Schema(description = "id автора объявления")
        private long author;

        @Schema(description = "Ссылка на картинку объявления")
        private String image;

        @Schema(description = "id объявления")
        private long pk;

        @Schema(description = "Цена объявления")
        private int price;

        @Schema(description = "Заголовок объявления")
        private String title;
    }
}
