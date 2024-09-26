package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.ImageUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserImageService;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import java.nio.file.Path;

@Service
public class UserImageServiceImpl implements UserImageService {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${path.to.avatar_user.folder}")
    private String avatarUserDir;

    public UserImageServiceImpl(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    public void uploadUserImage(String userEmail, MultipartFile multipartFile) throws IOException {

//        Optional<String> userEmailOptional = authService.getCurrentUserEmail();
//
//        if (userEmailOptional.isEmpty()) {
//            throw new NoSuchElementException("The user is not: authorized or registered" + userEmail);
//        }
//
//        Optional<User> userOptional = userRepository.findByEmail(userEmail);
//
//        if (userOptional.isEmpty()) {
//            throw new NoSuchElementException("User not found with email: " + userEmail);
//        }
//
//        if (userEmailOptional.get().isEmpty()) {
//            throw new NoSuchElementException("User not found with email: " + userEmail);
//        }
//
//        if (!fileTypeMatchingService.isValidImage(multipartFile)) {
//            throw new IllegalArgumentException("Invalid file type. Please upload an image file.");
//        }
//
//        String idFileName = userOptional.map(Object::toString).orElse("defaultId");
//
//        Path filePath = Path.of(avatarUserDir, idFileName + "_" + userEmail.toUpperCase() + "." + fileUtilityService.getExtensions(Objects.requireNonNull(multipartFile.getOriginalFilename())));
//
//        fileUtilityService.saveFile(multipartFile, filePath);
//
//        ImageUser imageUser = findImageByUserId(userOptional.get().getId());
//        imageUser.setUser(userOptional.get());
//        imageUser.setFilePath(filePath.toString());
//        imageUser.setFileSize(multipartFile.getSize());
//
//        String contentType = multipartFile.getContentType();
//
//        if (contentType == null) {
//            throw new IllegalStateException("Content type of file is null");
//        }
//
//        imageUser.setMediaType(contentType);
//        imageUser.setDataForm(fileUtilityService.generateFileForDataBase(filePath, 200));
//        imageUserRepository.save(imageUser);
    }
}
