package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {

    private long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] dataForm;
    private Long relatedId;  // Для связи с User или Ad (некий универсальный вариант)
}
