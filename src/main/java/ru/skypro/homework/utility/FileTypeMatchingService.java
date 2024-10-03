package ru.skypro.homework.utility;

import org.springframework.web.multipart.MultipartFile;

public interface FileTypeMatchingService {
    boolean isValidImage(MultipartFile file);
}
