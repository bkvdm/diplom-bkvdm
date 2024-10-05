package ru.skypro.homework.utility.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.utility.FileTypeMatchingService;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileTypeMatchingServiceImpl implements FileTypeMatchingService {

    /**
     * Проверяет, является ли предоставленный файл изображением.
     * <p>
     * Этот метод пытается открыть поток ввода из файла и считать его с помощью ImageIO.
     * Если файл успешно считывается как изображение, метод возвращает true.
     * В противном случае, если файл не может быть считан как изображение, возвращается false.
     *
     * @param file файл, который необходимо проверить.
     * @return true, если файл успешно считан как изображение, иначе false.
     * @throws RuntimeException если возникает IOException при чтении файла.
     */
    @Override
    public boolean isValidImage(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            try {
                ImageIO.read(is).toString();
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file (not image format)", e);
        }
    }
}
