package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.Map;

/**
 * REST-контроллер для управления пользователями.
 * Обрабатывает запросы на обновление информации о пользователе, его пароля и аватара.
 */
@RestController
@Tag(name = "Пользователи")
@RequestMapping("/users")
public class UserController {

    /**
     * Обновляет пароль авторизованного пользователя.
     *
     * @param newPassword карта с новыми значениями пароля, где ключ – это старый пароль и новый пароль
     * @return сообщение о статусе обновления пароля
     */
    @Operation(summary = "Обновление пароля",
            description = "Метод обновления пароля зарегистрированного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пароль успешно обновлён"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные пароля", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Не авторизован", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword(@RequestBody Map<String, String> newPassword) {
        // TODO: Логика в методе класса сервиса для обновления пароля
        return ResponseEntity.ok("Password updated successfully.");
    }

    /**
     * Получает информацию об авторизованном пользователе.
     *
     * @return информация о текущем авторизованном пользователе
     */
    @Operation(summary = "Получение информации об авторизованном пользователе",
            description = "Метод получения информации о зарегистрированном пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена"),
                    @ApiResponse(responseCode = "401", description = "Не авторизован", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        // TODO: Логика в методе класса сервиса для получения информации о пользователе
        return ResponseEntity.ok(new User());
    }

    /**
     * Обновляет информацию о текущем авторизованном пользователе.
     *
     * @param updateUser объект с новыми данными пользователя
     * @return обновлённая информация о пользователе
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе",
            description = "Метод обновления информации о зарегистрированном пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно обновлена"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Не авторизован", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody User updateUser) {
        // TODO: Логика в методе класса сервисаа для обновления информации о пользователе
        return ResponseEntity.ok(updateUser);
    }

    /**
     * Обновляет аватар текущего авторизованного пользователя.
     *
     * @param image файл изображения, который будет установлен как аватар
     * @return сообщение о статусе обновления аватара
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя",
            description = "Метод обновления аватара зарегистрированного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар успешно обновлён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный формат изображения", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Не авторизован", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            })
    @PatchMapping("/me/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image) {
        // TODO: Логика в методе класса обновления аватара
        return ResponseEntity.ok("User image updated successfully.");
    }
}
