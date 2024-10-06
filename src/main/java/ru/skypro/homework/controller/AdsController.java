package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdReview;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdReviewMapper;
import ru.skypro.homework.model.Ad;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import ru.skypro.homework.service.AdImageService;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST-контроллер для управления объявлениями.
 * Обрабатывает запросы на создание, обновление, удаление и получение объявлений.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Объявления")
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;
    private final AdReviewMapper adReviewMapper;
    private final AdImageService adImageService;

    public AdsController(AdService adService, AdReviewMapper adReviewMapper, AdImageService adImageService) {
        this.adService = adService;
        this.adReviewMapper = adReviewMapper;
        this.adImageService = adImageService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AdsController.class);

    /**
     * Получение всех объявлений.
     *
     * @return объект Ads, содержащий количество объявлений и список всех объявлений.
     */
    @Operation(summary = "Получение всех объявлений",
            description = "Метод получения всех объявлений, возвращает количество объявлений и их список.",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех объявлений успешно получен"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @GetMapping
    public ResponseEntity<AdReview> getAllAds() {
        List<AdReview.AdResult> adResults = new ArrayList<>();

        AdReview adReview = adService.getAllAds();
        return ResponseEntity.ok(adReview);
    }

    /**
     * Эндпоинт для создания нового объявления.
     * <p>
     * Этот метод принимает данные для нового объявления, включая его свойства и изображение.
     * Данные передаются в формате multipart/form-data.
     * После успешного создания объявления возвращается объект {@link Ad}, содержащий информацию
     * об объявлении, включая автора, ссылку на изображение, ID, цену и заголовок.
     * </p>
     *
     * @param properties Свойства нового объявления, переданные через форму. Поля включают заголовок, описание и цену.
     * @param image      Изображение для объявления (обязательный параметр). Изображение будет загружено и связано с объявлением.
     * @return {@link ResponseEntity}, содержащее объект {@link Ad} с данными созданного объявления.
     * В случае успеха возвращается статус 201 CREATED.
     * @throws IOException если произошла ошибка при загрузке изображения.
     */
    @Operation(summary = "Добавление объявления",
            description = "Метод для добавления нового объявления. Возвращает информацию о созданном объявлении.",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Объявление успешно добавлено"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdReview.AdResult> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image) throws IOException {

        AdReview.AdResult createdAd = adService.addingAd(properties, image);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    /**
     * Удаление объявления по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return пустой ответ с кодом 204.
     */
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Удаление объявления",
            description = "Метод для удаления объявления", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @DeleteMapping("/{id}")
    @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") long id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение информации об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return расширенная информация о выбранном объявлении.
     */
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Получение информации об объявлении",
            description = "Метод для получения информации об объявлении", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация об объявлении успешно получена"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @GetMapping("/{id}")
    @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable("id") long id) {
        ExtendedAd ad = adService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    /**
     * Обновление объявления по его идентификатору.
     *
     * @param id         идентификатор объявления.
     * @param updateData обновленные данные объявления.
     * @return обновленное объявление.
     */
    @PreAuthorize("isAuthenticated() and @userService.getCurrentUserId() == @adService.getAdAuthorId(#id)")
    @Operation(summary = "Обновление информации об объявлении",
            description = "Метод обновления информации об объявлении", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @PatchMapping("/{id}")
    @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1")
    public ResponseEntity<AdReview.AdResult> updateAd(
            @PathVariable("id") long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO объявления для его обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateAd.class))
            ) @RequestBody CreateOrUpdateAd updateData) {
        Ad updatedAd = adService.updateAd(id, updateData);
        AdReview.AdResult result = adReviewMapper.toAdResult(updatedAd);
        return ResponseEntity.ok(result);
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return объект Ads, содержащий количество объявлений и список объявлений текущего пользователя..
     */
    @Operation(summary = "Получение всех объявлений авторизированного пользователя",
            description = "Метод получения списка объявлений текущего пользователя, возвращает количество объявлений и их список по авторизированному пользователю.",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех объявлений успешно получен"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @GetMapping("/me")
    public ResponseEntity<AdReview> getAllAdsMe() {
        List<AdReview.AdResult> adResults = new ArrayList<>();
        AdReview adReview = adService.getAllAdsForCurrentUser();
        return ResponseEntity.ok(adReview);
    }

    /**
     * Обновление картинки для объявления по его идентификатору.
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение для объявления.
     * @return сообщение об успешном обновлении изображения.
     */
    @Operation(summary = "Обновление картинки объявления",
            description = "Метод обновления изображения к объявлению", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
                    @ApiResponse(responseCode = "400", description = "Некорректное изображение", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            })
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated() and @userService.getCurrentUserId() == @adService.getAdAuthorId(#id)")
    public ResponseEntity<String> updateImage(
            @Parameter(name = "id", description = "Идентификатор объявления", required = true, example = "1") @PathVariable("id") long id,
            @Parameter(name = "file", description = "Файл картинки в формате multipart", required = true) @RequestParam("image") MultipartFile image) {
        try {
            adImageService.renewImageAdByIdAd(id, image);
            return ResponseEntity.ok("Image updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Объявление не найдено.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера: " + e.getMessage());
        }
    }
}
