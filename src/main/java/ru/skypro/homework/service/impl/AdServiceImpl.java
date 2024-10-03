package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdReview;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdReviewMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdImageService;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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

//        Ad ad = adMapper.toEntity(createOrUpdateAd);
//        ad.setUser(user);

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


//    @Override
//    public void addingAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile multipartFile) throws IOException {
//
//        logger.info("Object createOrUpdateAd, contains: {}", createOrUpdateAd.toString());
//
//        UserDto userDto = userService.getCurrentUser();
//
//        User user = userService.getUserFromUserDtoExtend(userDto);
//
////        long userid = userService.getCurrentUserId();
////
////        User user = userService.findById(userId)
////                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
//
//        Ad ad = adMapper.toEntity(createOrUpdateAd);
//
//
//        ad.setUser(user);
//        if (multipartFile != null && !multipartFile.isEmpty()) {
//            adRepository.save(ad);
//        } else {
//            adImageService.uploadAdImage(ad, multipartFile);
//        }
//
////        Optional<Ad> adSaveOptional = adRepository.findTopByUserIdOrderByIdDesc(user.getId());
////
////        if (adSaveOptional.isEmpty()) {
////            throw new NoSuchElementException("At not found: " + ad.getTitle());
////        }
//
////        adImageService.uploadAdImage(adSaveOptional.get(), multipartFile);
//    }

//    /**
//     * Получение всех объявлений.
//     *
//     * @return объект AdReview, содержащий количество объявлений и список всех объявлений.
//     */
//    @Override
//    public AdReview getAllAds() {
//        List<Ad> ads = adRepository.findAll(); // Получаем все объявления из репозитория
//        List<AdReview.AdResult> adResults = ads.stream()
//                .map(adReviewMapper::toAdResult) // Преобразуем каждое объявление в AdReview.AdResult
//                .collect(Collectors.toList());
//
//        AdReview response = new AdReview();
//        response.setCount(adResults.size()); // Устанавливаем количество объявлений
//        response.setResults(adResults); // Устанавливаем список объявлений
//
//        return response;
//    }

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
}
