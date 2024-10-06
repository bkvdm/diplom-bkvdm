package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import ru.skypro.homework.service.AdImageService;
import ru.skypro.homework.service.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

@CrossOrigin(value = "http://localhost:3000")
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
     * Получение изображения по имени файла.
     *
     * <p>
     * Этот метод извлекает изображение по указанному имени файла из заданной директории.
     * Если изображение найдено, оно возвращается с соответствующим MIME-типом.
     * Если файл не найден, возвращается статус 404 NOT FOUND.
     * При возникновении ошибки чтения файла возвращается статус 500 INTERNAL SERVER ERROR.
     * </p>
     *
     * @param filename имя файла изображения, который необходимо получить.
     * @return {@link ResponseEntity}, содержащий изображение в виде массива байт и соответствующий HTTP статус.
     * @throws RuntimeException в случае ошибок при чтении файла.
     */
    @Operation(
            summary = "Получение изображения по имени файла",
            description = "Метод возвращает изображение по указанному имени файла из заданной директории.",
            tags = {"Изображения"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно получено", content = @Content(mediaType = "image/jpeg")),
                    @ApiResponse(responseCode = "404", description = "Файл не найден", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка при чтении файла", content = @Content)
            })
    @GetMapping("/{filename:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            // Генерация полного пути к файлу
            Path filePath = Paths.get(imageAdDirectory, filename);

            byte[] imageBytes = imageService.getImage(filePath);

            if (imageBytes == null) {
                return ResponseEntity.notFound().build();
            }

            // Определение MIME-типа файла с помощью сервиса
            String contentType = imageService.getContentType(filePath);

            // Возврат файла с соответствующим Content-Type
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (IOException e) {
            throw new RuntimeException("Error reading image file: " + filename, e);
        }
    }
}
