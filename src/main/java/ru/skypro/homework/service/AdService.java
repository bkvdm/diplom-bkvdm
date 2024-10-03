package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdReview;
import ru.skypro.homework.dto.CreateOrUpdateAd;

import java.io.IOException;

public interface AdService {
    void addingAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile multipartFile) throws IOException;

    AdReview getAllAds();

    AdReview getAllAdsForCurrentUser();
}
