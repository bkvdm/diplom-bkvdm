package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import ru.skypro.homework.service.ImageService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/images")
public class ImageAdController {

    public final ImageService imageService;

    public ImageAdController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Получение изображения объявления по его ID.
     * <p>
     * Этот эндпоинт предоставляет изображение объявления, извлекая его из базы данных по ID.
     * Логика получения изображения делегирована сервису {@link ImageService}, который формирует
     * ответ с изображением и заголовками, определяющими тип контента и имя файла.
     * </p>
     *
     * @param id ID изображения, связанного с объявлением
     * @return {@link ResponseEntity}, содержащее изображение в формате byte[].
     */
    @Operation(
            summary = "Получение изображения объявления по ID",
            description = "Метод возвращает изображение, связанное с объявлением по его уникальному ID. Если изображение найдено, оно возвращается в ответе с типом контента и заголовками.",
            tags = {"Изображения"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно получено", content = @Content(mediaType = "image/jpeg")),
                    @ApiResponse(responseCode = "404", description = "Изображение не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            }
    )
//    @GetMapping("/images/{id}")
    @GetMapping("{id}")
    public ResponseEntity<byte[]> serveCurrentImageAd(@PathVariable long id) {

        try {
            return imageService.getImageAdResponse(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
