package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;

public interface ImageService {
    ResponseEntity<byte[]> getAvatarResponseByEmail(String userEmail);

    ResponseEntity<byte[]> getImageAdResponse(long idImage);
}
