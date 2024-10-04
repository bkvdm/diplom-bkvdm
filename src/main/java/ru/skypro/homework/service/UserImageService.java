package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.ImageUser;

import java.io.IOException;

public interface UserImageService {
    void uploadUserImage(String userEmail, MultipartFile multipartFile) throws IOException;

    ImageUser findImageByUserId(long userId);
}
