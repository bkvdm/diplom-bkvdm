package ru.skypro.homework.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdReview;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;

import java.io.IOException;

public interface AdService {

    AdReview.AdResult addingAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile multipartFile) throws IOException;

    AdReview getAllAds();

    AdReview getAllAdsForCurrentUser();

    ExtendedAd getAdById(long id);

    void deleteAd(long idAd);

    @Transactional
    Ad updateAd(long id, CreateOrUpdateAd updateData);
}
