package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.ImageUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageUserRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserImageService;
import ru.skypro.homework.utility.FileTypeMatchingService;
import ru.skypro.homework.utility.FileUtilityService;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import java.nio.file.Path;

@Service
public class UserImageServiceImpl implements UserImageService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final FileTypeMatchingService fileTypeMatchingService;
    private final FileUtilityService fileUtilityService;
    private final ImageUserRepository imageUserRepository;

    @Value("${path.to.avatar_user.folder}")
    private String avatarUserDir;

    public UserImageServiceImpl(AuthService authService, UserRepository userRepository, FileTypeMatchingService fileTypeMatchingService, FileUtilityService fileUtilityService, ImageUserRepository imageUserRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.fileTypeMatchingService = fileTypeMatchingService;
        this.fileUtilityService = fileUtilityService;
        this.imageUserRepository = imageUserRepository;
    }

    /**
     * Загружает изображение пользователя и сохраняет его на сервере.
     *
     * <p>Метод принимает адрес электронной почты пользователя и файл изображения в формате
     * {@code MultipartFile}. Он проверяет, авторизован ли пользователь, а также
     * валиден ли тип загружаемого файла. В случае успешной проверки, файл
     * сохраняется на сервере, а информация о пользователе и изображении
     * обновляется в базе данных. Если происходит ошибка на любом этапе,
     * выбрасываются соответствующие исключения.</p>
     *
     * @param userEmail     адрес электронной почты пользователя, для которого загружается изображение
     * @param multipartFile файл изображения, который нужно загрузить
     * @throws IOException              если произошла ошибка при сохранении файла
     * @throws NoSuchElementException   если пользователь не авторизован или не найден в базе данных
     * @throws IllegalArgumentException если тип файла не соответствует требованиям
     * @throws IllegalStateException    если тип содержимого файла равен {@code null}
     */
    @Override
    public void uploadUserImage(String userEmail, MultipartFile multipartFile) throws IOException {

        Optional<String> userEmailOptional = authService.getCurrentUserEmail();

        if (userEmailOptional.isEmpty()) {
            throw new NoSuchElementException("The user is not: authorized or registered" + userEmail);
        }

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found with email: " + userEmail);
        }

        if (userEmailOptional.get().isEmpty()) {
            throw new NoSuchElementException("User not found with email: " + userEmail);
        }

        if (!fileTypeMatchingService.isValidImage(multipartFile)) {
            throw new IllegalArgumentException("Invalid file type. Please upload an image file.");
        }

        String idFileName = String.valueOf(userOptional.get().getId());

        Path filePath = Path.of(avatarUserDir, idFileName + "_" + userEmail.toUpperCase() + "." + fileUtilityService.getExtensions(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        try {
            fileUtilityService.saveFile(multipartFile, filePath);
        } catch (IOException e) {
            throw new IOException("Error saving the file", e);
        }

        ImageUser imageUser = findImageByUserId(userOptional.get().getId());
        imageUser.setUser(userOptional.get());
        imageUser.setFilePath("/" + filePath.toString().replace(File.separator, "/"));
        imageUser.setFileSize(multipartFile.getSize());

        String contentType = multipartFile.getContentType();

        if (contentType == null) {
            throw new IllegalStateException("Content type of file is null");
        }

        imageUser.setMediaType(contentType);
        imageUser.setDataForm(fileUtilityService.generateFileForDataBase(filePath, 800));
        imageUserRepository.save(imageUser);
    }

    /**
     * Находит изображение пользователя по его идентификатору.
     *
     * <p>Метод ищет изображение, связанное с пользователем, по его уникальному
     * идентификатору. Если изображение не найдено, возвращается новый экземпляр
     * {@code ImageUser}.</p>
     *
     * @param userId идентификатор пользователя, для которого нужно найти изображение
     * @return объект {@code ImageUser}, содержащий информацию об изображении пользователя
     */
    @Override
    public ImageUser findImageByUserId(long userId) {
        return imageUserRepository.findByUserId(userId).orElse(new ImageUser());
    }
}
