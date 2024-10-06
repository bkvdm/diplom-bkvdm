package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentResult {

    @Schema(description = "id автора объявления")
    private long author;

    @Schema(description = "Дата и время создания комментария в миллисекундах с 00:00:00 09.05.1945")
    private String createdAt;

    @Schema(description = "id комментария")
    private long pk;

    @Schema(description = "Текст комментария")
    private String text;
}
