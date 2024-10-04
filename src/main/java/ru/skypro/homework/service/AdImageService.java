package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.ImageAd;

import java.io.IOException;

public interface AdImageService {
    void uploadAdImage(Ad ad, MultipartFile multipartFile) throws IOException;

    void renewImageAdByIdAd(long idAd, MultipartFile multipartFile) throws IOException;

    ImageAd findImageByAdId(long adId);
}
