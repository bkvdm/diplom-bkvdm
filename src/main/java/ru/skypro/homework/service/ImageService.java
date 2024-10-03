package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.ImageDto;

import java.util.Optional;

public interface ImageService {
    ResponseEntity<byte[]> getAvatarResponseByEmail(String userEmail);

    ResponseEntity<byte[]> getImageAdResponse(long idImage);
}
