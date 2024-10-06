package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

    ResponseEntity<byte[]> getAvatarResponseByEmail(String userEmail);

    byte[] getImage(Path filePath) throws IOException;

    String getContentType(Path filePath) throws IOException;
}
