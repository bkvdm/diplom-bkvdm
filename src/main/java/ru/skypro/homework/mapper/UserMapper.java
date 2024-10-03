package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CommentReview;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.ImageUser;
import ru.skypro.homework.model.User;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    // Для преобразования User -> Login
    @Mapping(source = "email", target = "username")
    Login toLoginDto(User user);

    // Для преобразования Register -> User
    @Mapping(source = "username", target = "email")
    User toEntity(Register register);

    // Для преобразования User -> Register
    @Mapping(source = "email", target = "username")
    Register toRegisterDto(User user);

    // Для преобразования User -> UserDto
    @Mapping(source = "imageUser.filePath", target = "image")  // Преобразование filePath в строку image
    UserDto toUserDto(User user);

    // Для преобразования UserDto -> User
    @Mapping(target = "imageUser", source = "image")  // Преобразование строки image обратно в объект ImageUser
    User toEntity(UserDto userDto);

    // Дополнительный метод для преобразования строки image в объект ImageUser
    default ImageUser map(String imagePath) {
        if (imagePath == null) {
            return null;
        }
        ImageUser imageUser = new ImageUser();
        imageUser.setFilePath(imagePath);  // Указан путь к файлу filePath
        return imageUser;
    }
}
