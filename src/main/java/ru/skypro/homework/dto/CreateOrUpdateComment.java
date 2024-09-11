package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateComment {

    // Поле id можно не включать, если оно генерируется автоматически при создании
    private String text; // Текст комментария

    private long userId; // Идентификатор пользователя, который оставляет комментарий
}