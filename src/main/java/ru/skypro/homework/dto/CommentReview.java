package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentReview {

    private int count;
    private List<CommentReview.CommentResult> results;

    @Data
    public static class CommentResult {

        @Schema(description = "id автора объявления")
        private long author;

        @Schema(description = "Ссылка на аватар автора комментария")
        private String authorImage;

        @Schema(description = "Имя создателя комментария")
        private String authorFirstName;

        @Schema(description = "Дата и время создания комментария в миллисекундах с 00:00:00 09.05.1945")
        private LocalDateTime createdAt;

        @Schema(description = "id комментария")
        private long pk;

        @Schema(description = "Текст комментария")
        private String text;
    }
}
