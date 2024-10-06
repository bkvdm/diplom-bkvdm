package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

public interface UserService {
    UserDto getCurrentUser();

    void updateUser(UserDto userDto);

    void updatePassword(NewPassword newPassword);

    User getUserFromUserDtoExtend(UserDto userDto);

    Long getCurrentUserId();

    boolean isCurrentUserAdmin();
}
