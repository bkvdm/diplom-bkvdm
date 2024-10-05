package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.AdImageService;
import ru.skypro.homework.service.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.nio.file.Path;

@RestController
@RequestMapping("/image_ad")
public class ImageAdController {

    @Value("${path.to.image_ad.folder}")
    private String imageAdDirectory;

    public final ImageService imageService;
    public final AdImageService adImageService;

    public ImageAdController(ImageService imageService, AdImageService adImageService) {
        this.imageService = imageService;
        this.adImageService = adImageService;
    }
    /**
     * REST-контроллер для получения изображений по адресу, запрошенный фрондом.
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            // Генерация полного пути к файлу
            Path filePath = Paths.get(imageAdDirectory, filename);

            // Проверка существования файла
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Определение MIME-типа файла
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Чтение файла как массива байт
            byte[] imageBytes = Files.readAllBytes(filePath);

            // Возврат файла с соответствующим Content-Type
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (IOException e) {
            throw new RuntimeException("Error reading image file: " + filename, e);
        }
    }
}
