package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentReview;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.List;

/**
 * REST-контроллер для управления комментариями.
 * Обрабатывает запросы на создание, обновление, удаление и получение комментариев.
 */
@RestController
@Tag(name = "Комментарии")
@RequestMapping("/ads/{adId}/comments")
public class CommentsController {

    /**
     * Получает список комментариев, связанных с указанным объявлением.
     *
     * @param adId идентификатор объявления, для которого нужно получить комментарии
     * @return список комментариев, связанных с объявлением
     */
    @Operation(summary = "Получение комментариев объявления",
            description = "Метод для получения комментариев к объявлению", tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарии успешно получены"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @GetMapping
    public ResponseEntity<List<CommentReview>> getComments(
            @Parameter(description = "Идентификатор объявления", required = true, example = "1")
            @PathVariable("adId") long adId) {
        // TODO: Логика в классе сервиса для получения комментариев
        return ResponseEntity.ok(List.of(new CommentReview()));
    }

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * @param adId        идентификатор объявления, к которому добавляется комментарий
     * @param commentData данные для создания нового комментария
     * @return созданный комментарий
     */
    @Operation(summary = "Добавление комментария к объявлению",
            description = "Метод для добавления комментариев к объявлению", tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Комментарий успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные для комментария", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @PostMapping
    public ResponseEntity<CommentReview.CommentResult> addComment(
            @Parameter(description = "Идентификатор объявления", required = true, example = "1")
            @PathVariable("adId") long adId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO комментария для его обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateComment.class))
            ) @RequestBody CreateOrUpdateComment commentData) {
        // TODO: Логика в классе сервиса добавления комментария
        return ResponseEntity.ok(new CommentReview.CommentResult());
    }

    /**
     * Удаляет комментарий по идентификатору.
     *
     * @param adId      идентификатор объявления, к которому относится комментарий
     * @param commentId идентификатор комментария, который нужно удалить
     * @return статус ответа 200 OK при успешном удалении
     */
    @Operation(summary = "Удаление комментария",
            description = "Метод для удаления комментариев к объявлению", tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Комментарий или объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Идентификатор объявления", required = true, example = "1")
            @PathVariable("adId") long adId,
            @Parameter(description = "Идентификатор комментария", required = true, example = "1")
            @PathVariable("commentId") long commentId) {
        // TODO: Логика в классе сервиса (метода) для удаления комментария
        return ResponseEntity.ok().build();
    }

    /**
     * Обновляет существующий комментарий.
     *
     * @param adId        идентификатор объявления, к которому относится комментарий
     * @param commentId   идентификатор комментария, который нужно обновить
     * @param commentData данные для обновления комментария
     * @return обновлённый комментарий
     */
    @Operation(summary = "Обновление комментария",
            description = "Метод для обновления комментария", tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления комментария", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Комментарий или объявление не найдено", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentReview.CommentResult> updateComment(
            @Parameter(description = "Идентификатор объявления", required = true, example = "1")
            @PathVariable("adId") long adId,
            @Parameter(description = "Идентификатор комментария", required = true, example = "1")
            @PathVariable("commentId") long commentId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO комментария для его обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateComment.class))
            ) @RequestBody CreateOrUpdateComment commentData) {
        // TODO: Логика в классе сервиса для обновления комментария
        return ResponseEntity.ok(new CommentReview.CommentResult());
    }
}
