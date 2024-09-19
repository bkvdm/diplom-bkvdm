package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.List;

/**
 * REST-контроллер для управления объявлениями.
 * Обрабатывает запросы на создание, обновление, удаление и получение объявлений.
 */
@RestController
@Tag(name = "Объявления")
@RequestMapping("/ads")
public class AdsController {

    /**
     * Получение всех объявлений.
     *
     * @return список всех объявлений.
     */
    @Operation(summary = "Получение всех объявлений",
            description = "Метод получения всех объявлений", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех объявлений успешно получен"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @GetMapping
    public ResponseEntity<List<Ad>> getAllAds() {
        // TODO: Дополнить логику получения всех объявлений в сервисе получения всех объявлений
        return ResponseEntity.ok(List.of(new Ad()));
    }

    /**
     * Добавление нового объявления.
     *
     * @param image        изображение для объявления.
     * @param adProperties свойства нового объявления.
     * @return созданное объявление.
     */
    @Operation(summary = "Добавление объявления",
            description = "Метод для добавления объявления", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Объявление успешно добавлено"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @PostMapping
    public ResponseEntity<Ad> addAd(
            @RequestParam("image") MultipartFile image,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO объявления для его обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateAd.class))
            ) @RequestParam("properties") CreateOrUpdateAd adProperties) {
        // TODO: Дополнить логику добавления объявления в сервисе
        return ResponseEntity.status(201).body(new Ad());
    }

    /**
     * Получение информации об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return расширенная информация о выбранном объявлении.
     */
    @Operation(summary = "Получение информации об объявлении",
            description = "Метод для получения информации об объявлении", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация об объявлении успешно получена"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @GetMapping("/{id}")
    @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable("id") long id) {
        // TODO: Дополнить логику получения объявления по значению id объявления
        return ResponseEntity.ok(new ExtendedAd());
    }

    /**
     * Удаление объявления по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return пустой ответ с кодом 204.
     */
    @Operation(summary = "Удаление объявления",
            description = "Метод для удаления объявления", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @DeleteMapping("/{id}")
    @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") long id) {
        // TODO: Дополнить логику удаления объявления по значению идентификатора
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновление объявления по его идентификатору.
     *
     * @param id         идентификатор объявления.
     * @param updateData обновленные данные объявления.
     * @return обновленное объявление.
     */
    @Operation(summary = "Обновление информации об объявлении",
            description = "Метод обновления информации об объявлении", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @PatchMapping("/{id}")
    @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1")
    public ResponseEntity<Ad> updateAd(
            @PathVariable("id") long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO объявления для его обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateAd.class))
            ) @RequestBody CreateOrUpdateAd updateData) {
        // TODO: Дополнить логику обновления объявления по значению id объявления
        return ResponseEntity.ok(new Ad());
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return список объявлений текущего пользователя.
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя",
            description = "Метод для получения объявлений зарегистрированного пользователя", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявления успешно получены"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @GetMapping("/me")
    public ResponseEntity<List<Ad>> getMyAds() {
        // TODO: Дополнить логику получения объявлений авторизованного пользователя
        return ResponseEntity.ok(List.of(new Ad()));
    }

    /**
     * Обновление картинки для объявления по его идентификатору.
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение для объявления.
     * @return сообщение об успешном обновлении изображения.
     */
    @Operation(summary = "Обновление картинки объявления",
            description = "Метод обновления изображения к объявлению", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
                    @ApiResponse(responseCode = "400", description = "Некорректное изображение", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateImage(
            @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1") @PathVariable("id") long id,
            @Parameter(name = "file", description = "Файл картинки в формате multipart", required = true) @RequestParam("image") MultipartFile image) {
        // TODO: Дополнить логику обновления картинки объявления по id объявления
        return ResponseEntity.ok("Image updated successfully.");
    }
}
