package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.AdReview;
import ru.skypro.homework.model.Ad;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdReviewMapper {

//    AdReviewMapper INSTANCE = Mappers.getMapper(AdReviewMapper.class);

    // Для преобразования Ad -> AdResult
    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "imageAd.filePath", target = "image")
    @Mapping(source = "id", target = "pk")
    AdReview.AdResult toAdResult(Ad ad);

    // Для преобразования списка Ad -> AdReview
    default AdReview toAdReview(List<Ad> ads) {
        List<AdReview.AdResult> adResults = ads.stream()
                .map(this::toAdResult)
                .collect(Collectors.toList());
        AdReview adReview = new AdReview();
        adReview.setCount(adResults.size());
        adReview.setResults(adResults);
        return adReview;
    }
}
