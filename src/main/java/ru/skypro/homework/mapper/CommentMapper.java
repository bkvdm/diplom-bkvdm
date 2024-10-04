package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "localDateTime", target = "createdAt")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "adId", target = "ad.id")
    Comment toEntity(CreateOrUpdateComment dto);

    @Mapping(source = "createdAt", target = "localDateTime")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "ad.id", target = "adId")
    CreateOrUpdateComment toDto(Comment comment);
}
