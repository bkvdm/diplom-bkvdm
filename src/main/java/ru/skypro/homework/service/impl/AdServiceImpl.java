package ru.skypro.homework.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.AdReview;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdReviewMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdImageService;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdServiceImpl implements AdService {

    private final UserService userService;
    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final AdImageService adImageService;
    private final AdReviewMapper adReviewMapper;

    public AdServiceImpl(UserService userService, AdMapper adMapper, AdRepository adRepository, AdImageService adImageService, AdReviewMapper adReviewMapper) {
        this.userService = userService;
        this.adMapper = adMapper;
        this.adRepository = adRepository;
        this.adImageService = adImageService;
        this.adReviewMapper = adReviewMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    @Override
    public void addingAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile multipartFile) throws IOException {
        logger.info("Object createOrUpdateAd, contains: {}", createOrUpdateAd.getDescription());
        logger.info("Object file in service method: {}", multipartFile.getSize());

        logger.info("Ad Title: {}", createOrUpdateAd.getTitle());
        logger.info("Ad Description: {}", createOrUpdateAd.getDescription());

        if (createOrUpdateAd.getTitle() == null || createOrUpdateAd.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        UserDto userDto = userService.getCurrentUser();
        User user = userService.getUserFromUserDtoExtend(userDto);

        Ad ad = Ad.builder()
                .title(createOrUpdateAd.getTitle())
                .price(createOrUpdateAd.getPrice())
                .description(createOrUpdateAd.getDescription())
                .user(user)
                .build();

        logger.info("User into ad email {}, id user {}", user.getEmail(), user.getId());
        logger.info("Ad info with user set: id {}, title {}", ad.getId(), ad.getTitle());

        logger.info("Ad to be saved: id={}, title={}, description={}, userId={}",
                ad.getId(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getUser() != null ? ad.getUser().getId() : "null");

        Ad savedAd = adRepository.save(ad);
        logger.info("Ad save info, id Ad: {}, user: {}", savedAd.getId(), savedAd.getUser());

        if (!multipartFile.isEmpty()) {
            adImageService.uploadAdImage(savedAd, multipartFile);
        }
    }

    /**
     * Получение всех объявлений.
     *
     * @return объект AdReview, содержащий количество объявлений и список всех объявлений.
     */
    public AdReview getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return adReviewMapper.toAdReview(ads);
    }

    /**
     * Получение объявлений текущего авторизованного пользователя.
     *
     * @return объект AdReview, содержащий количество объявлений и список объявлений текущего пользователя.
     */
    public AdReview getAllAdsForCurrentUser() {
        long userId = userService.getCurrentUserId();
        List<Ad> ads = adRepository.findByUserId(userId); // Получаем объявления по userId
        return adReviewMapper.toAdReview(ads);
    }

    /**
     * Получение информации об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return DTO с информацией об объявлении.
     * @throws IllegalArgumentException если объявление не найдено.
     */
    @Override
    public ExtendedAd getAdById(long id) {
        Optional<Ad> adOptional = adRepository.findById(id);

        if (adOptional.isEmpty()) {
            throw new IllegalArgumentException("Объявление не найдено с идентификатором: " + id);
        }

        return adMapper.toExtendedDto(adOptional.get());
    }

    /**
     * Удаление объявления по его идентификатору.
     *
     * @param idAd идентификатор объявления.
     * @throws EntityNotFoundException если объявление не найдено.
     * @throws AccessDeniedException   если пользователь не является автором объявления.
     */
    @Override
    @Transactional
    public void deleteAd(long idAd) {

        long userId = userService.getCurrentUserId();
        logger.info("Found current user, with id: {}", userId);
        Ad ad = adRepository.findById(idAd)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено с идентификатором: " + idAd));

        logger.info("Find ad with id: {}", ad.getId());

        if (ad.getUser().getId() != userId) {
            throw new AccessDeniedException("У вас нет прав для удаления этого объявления");
        }

        try {
            adRepository.delete(ad);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при удалении объявления", e);
        }
    }

    /**
     * Обновление объявления по его идентификатору.
     *
     * @param id         идентификатор объявления.
     * @param updateData обновленные данные объявления.
     * @return обновленное объявление.
     */
    @Override
    @Transactional
    public Ad updateAd(long id, CreateOrUpdateAd updateData) {

        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление с идентификатором " + id + " не найдено."));

        if (existingAd.getUser().getId() != userService.getCurrentUserId()) {
            throw new AccessDeniedException("У вас нет прав для обновления этого объявления");
        }

        existingAd.setTitle(updateData.getTitle());
        existingAd.setPrice(updateData.getPrice());
        existingAd.setDescription(updateData.getDescription());

        return adRepository.save(existingAd);
    }
}
