package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.ImageDto;
import ru.skypro.homework.mapper.ImageMapper;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.model.ImageUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageAdRepository;
import ru.skypro.homework.repository.ImageUserRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;
    private final UserRepository userRepository;
    private final ImageUserRepository imageUserRepository;
    private final ImageAdRepository imageAdRepository;

    @Autowired
    public ImageServiceImpl(ImageMapper imageMapper, UserRepository userRepository, ImageUserRepository imageUserRepository, ImageAdRepository imageAdRepository) {
        this.imageMapper = imageMapper;
        this.userRepository = userRepository;
        this.imageUserRepository = imageUserRepository;
        this.imageAdRepository = imageAdRepository;
    }

    public ImageDto getImageUserDto(ImageUser imageUser) {
        return imageMapper.imageUserToImageDto(imageUser);
    }

    public ImageDto getImageAdDto(ImageAd imageAd) {
        return imageMapper.imageAdToImageDto(imageAd);
    }

    /**
     * Извлекает изображение аватара пользователя по его email и возвращает его в виде {@link ResponseEntity}.
     * <p>
     * Этот метод находит пользователя по его email и извлекает аватар.
     * Если аватар найден, метод создает ответ с данными изображения и заголовками типа контента.
     * Если пользователь или изображение не найдены, метод возвращает исключение.
     * </p>
     *
     * @param userEmail Email пользователя, аватар которого запрашивается.
     * @return {@link ResponseEntity}, содержащее изображение аватара пользователя в виде byte
     * и типом контента и размещения.
     * @throws NoSuchElementException если пользователь или его аватар не найдены.
     */
    @Override
    public ResponseEntity<byte[]> getAvatarResponseByEmail(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("The user is not authorized or registered: " + userEmail);
        }

        Optional<ImageUser> imageUser = imageUserRepository.findByUserId(userOptional.get().getId());

        if (imageUser.isEmpty()) {
            throw new NoSuchElementException("The image not found for user by username: " + userEmail);
        }

        ImageDto imageDto = getImageUserDto(imageUser.get());

        MediaType mediaType = MediaType.parseMediaType(imageDto.getMediaType());
        String fileName = imageDto.getFilePath();
        byte[] data = imageDto.getDataForm();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(mediaType)
                .body(data);
    }
}
