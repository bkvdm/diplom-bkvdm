package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserImageService {
    void uploadUserImage (String userEmail, MultipartFile multipartFile) throws IOException;
}
