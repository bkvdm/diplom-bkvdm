package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

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
    @Mapping(source = "imageUser", target = "image")
    UserDto toUserDto(User user);

    // Для преобразования UserDto -> User
    @Mapping(source = "image", target = "imageUser")
    User toEntity(UserDto userDto);
}
