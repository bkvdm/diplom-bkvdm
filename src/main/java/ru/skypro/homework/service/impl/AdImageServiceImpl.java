package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageAdRepository;
import ru.skypro.homework.service.AdImageService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utility.FileTypeMatchingService;
import ru.skypro.homework.utility.FileUtilityService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdImageServiceImpl implements AdImageService {

    private final FileTypeMatchingService fileTypeMatchingService;
    private final FileUtilityService fileUtilityService;
    private final ImageAdRepository imageAdRepository;
    private final AdRepository adRepository;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AdImageService.class);

    @Value("${path.to.image_ad.folder}")
    private String imageAdDir;


    public AdImageServiceImpl(FileTypeMatchingService fileTypeMatchingService, FileUtilityService fileUtilityService, ImageAdRepository imageAdRepository, AdRepository adRepository, UserService userService) {
        this.fileTypeMatchingService = fileTypeMatchingService;
        this.fileUtilityService = fileUtilityService;
        this.imageAdRepository = imageAdRepository;
        this.adRepository = adRepository;
        this.userService = userService;
    }

    /**
     * Загружает изображение для объявления и сохраняет его на сервере, а также в базе данных.
     *
     * <p>Метод проверяет корректность типа файла, сохраняет изображение в файловой системе,
     * генерирует путь к файлу, сохраняет информацию об изображении в базе данных, включая
     * такие параметры, как размер файла, тип медиа и содержимое для хранения в БД.</p>
     *
     * @param adSave            Объявление, к которому прикрепляется изображение.
     * @param multipartFile Файл изображения, загружаемый пользователем.
     * @throws IOException              Если возникли ошибки при сохранении файла.
     * @throws IllegalArgumentException Если файл не является допустимым изображением.
     * @throws IllegalStateException    Если тип содержимого файла null.
     */
    @Override
    public void uploadAdImage(Ad adSave, MultipartFile multipartFile) throws IOException {

        logger.info("Ad into uploadAdImage: {}, {}, {}, {}, {}", adSave.getId(), adSave.getTitle(), adSave.getPrice(), adSave.getTitle(), adSave.getPrice());
        logger.info("MultipartFile into uploadAdImage: {}", multipartFile.getSize());

        if (!fileTypeMatchingService.isValidImage(multipartFile)) {
            throw new IllegalArgumentException("Invalid file type. Please upload an image file.");
        }

        Path filePath = Path.of(imageAdDir, adSave.getUser().getId() + "_" + adSave.getId() + "." + fileUtilityService.getExtensions(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        logger.info("Path for image: {}", filePath);

        try {
            fileUtilityService.saveFile(multipartFile, filePath);
            logger.info("Image file saved on file system.");
        } catch (IOException e) {
            throw new IOException("Error saving the file", e);
        }

        // Проверяем, существует ли изображение для этого объявления
        Optional<ImageAd> existingImageAd = imageAdRepository.findByAdId(adSave.getId());

        ImageAd imageAd;
        if (existingImageAd.isPresent()) {
            // Если изображение существует, обновляем его
            imageAd = existingImageAd.get();
            logger.info("Updating existing image for ad id: {}", adSave.getId());
        } else {
            // Если изображение не существует, создаем новое
            imageAd = new ImageAd();
            imageAd.setAd(adSave);
            logger.info("Creating new image for ad id: {}", adSave.getId());
        }

        imageAd.setFilePath("/" + filePath.toString().replace(File.separator, "/"));
        imageAd.setFileSize(multipartFile.getSize());

        String contentType = multipartFile.getContentType();
        if (contentType == null) {
            throw new IllegalStateException("Content type of file is null");
        }
        imageAd.setMediaType(contentType);
        imageAd.setDataForm(fileUtilityService.generateFileForDataBase(filePath, 600));

        try {
            imageAdRepository.save(imageAd);
            logger.info("Image ({}) saved successfully", imageAd.getMediaType());
        } catch (Exception e) {
            throw new RuntimeException("Error saving image to database", e);
        }

        adSave.setImageAd(imageAd);
        logger.info("Set image with id: ({}) on ad with id: ({}).", imageAd.getId(), adSave.getId());
    }

    @Override
    public void renewImageAdByIdAd(long idAd, MultipartFile multipartFile) throws IOException {
        Ad ad = adRepository.findById(idAd)
                .orElseThrow(() -> new NoSuchElementException("Ad with id " + idAd + ", not found."));

        Long currentUserId = userService.getCurrentUserId();
        logger.info("Current user found id: {}", currentUserId);

        uploadAdImage(ad, multipartFile);
    }

    /**
     * Находит изображение, связанное с объявлением по его идентификатору.
     *
     * <p>Метод ищет изображение (ImageAd), связанное с объявлением, по идентификатору объявления.
     * Если изображение не найдено, возвращается новый пустой объект ImageAd.</p>
     *
     * @param adId Идентификатор объявления, для которого необходимо найти изображение.
     * @return Найденное изображение (ImageAd), связанное с объявлением.
     * Если изображение не найдено, возвращается новый объект ImageAd.
     */
    @Override
    public ImageAd findImageByAdId(long adId) {
        return imageAdRepository.findByAdId(adId).orElse(new ImageAd());
    }
}
