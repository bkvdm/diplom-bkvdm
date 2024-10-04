package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.ImageDto;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.model.ImageUser;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageMapper {

//    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    // Для преобразования imageUser -> imageDto
    @Mapping(target = "relatedId", source = "user.id")
    ImageDto imageUserToImageDto(ImageUser imageUser);

    // Для преобразования imageAd -> imageDto
    @Mapping(target = "relatedId", source = "ad.id")
    ImageDto imageAdToImageDto(ImageAd imageAd);

    // Для преобразования imageDto -> imageUser
    @Mapping(target = "user.id", source = "relatedId")
    ImageUser imageDtoToImageUser(ImageDto imageDto);

    // Для преобразования imageDto -> imageAd
    @Mapping(target = "ad.id", source = "relatedId")
    ImageAd imageDtoToImageAd(ImageDto imageDto);
}

