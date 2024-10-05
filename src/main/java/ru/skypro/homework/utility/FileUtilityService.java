package ru.skypro.homework.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileUtilityService {
    void saveFile(MultipartFile formFile, Path filePath) throws IOException;

    String getExtensions(String fileName);

    byte[] generateFileForDataBase(Path filePath, int width) throws IOException;
}
