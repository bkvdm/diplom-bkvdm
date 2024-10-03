package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {

//    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    // Для преобразования CreateOrUpdateAd -> Ad
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "imageAdId", target = "imageAd.id")
    @Mapping(source = "title", target = "title")
    @Mapping(target = "id", ignore = true) // Игнорирование поля id
    Ad toEntity(CreateOrUpdateAd dto);

    // Для преобразования Ad -> CreateOrUpdateAd
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "imageAd.id", target = "imageAdId")
    @Mapping(source = "title", target = "title")
    CreateOrUpdateAd toCreateOrUpdateDto(Ad ad);

    // Для преобразования Ad -> ExtendedAd
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "imageAd.filePath", target = "image")
    @Mapping(source = "title", target = "title")
    ExtendedAd toExtendedDto(Ad ad);
}
