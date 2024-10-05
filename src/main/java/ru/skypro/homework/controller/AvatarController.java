package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.ImageService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.Optional;

@RestController
@RequestMapping("/avatar_user")
public class AvatarController {

    private final ImageService imageService;
    private final AuthService authService;

    @Autowired
    public AvatarController(ImageService imageService, AuthService authService) {
        this.imageService = imageService;
        this.authService = authService;
    }

    /**
     * Получение аватара текущего пользователя в виде byte ответа.
     * <p>
     * Этот эндпоинт извлекает аватар авторизованного пользователя по его email.
     * Если аватар найден, он возвращается в ответе с соответствующими заголовками и типом контента.
     * В случае, если пользователь не авторизован, возвращается статус 401 UNAUTHORIZED.
     * Если аватар не найден, возвращается статус 404 NOT FOUND.
     * </p>
     *
     * @return {@link ResponseEntity}, содержащее изображение аватара в виде byte[], либо соответствующий код ошибки.
     */
    @Operation(
            summary = "Получение аватара текущего пользователя",
            description = "Метод возвращает аватар текущего авторизованного пользователя по его email. Если аватар найден, возвращается изображение в ответе с заголовками.",
            tags = {"Аватары"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар успешно получен", content = @Content(mediaType = "image/jpeg")),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Аватар не найден", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
            }
    )
    @GetMapping(value = "/{image}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> serveCurrentUserAvatar() {

        Optional<String> currentUserEmail = authService.getCurrentUserEmail();

        return currentUserEmail
                .map(imageService::getAvatarResponseByEmail)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
