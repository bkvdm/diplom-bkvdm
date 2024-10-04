package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.ImageUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageUserRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper; // Внедрение маппера
    private final ImageUserRepository imageUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ImageUserRepository imageUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.imageUserRepository = imageUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or not authenticated: " + username));
    }

    @Transactional
    @Override
    public void updateUser(UserDto userDto) {
        UserDto userDtoAuth = getCurrentUser();

        // Загрузка текущего пользователя из базы данных
        User currentUser = currentUser(userDtoAuth);

        currentUser.setFirstName(userDto.getFirstName());
        currentUser.setLastName(userDto.getLastName());
        currentUser.setPhone(userDto.getPhone());

        // Проверка наличия ImageUser у User, и перезапись ImageUser с сохранением связи сущностей
        if (currentUser.getImageUser() != null) {
            ImageUser imageUser = currentUser.getImageUser();
            imageUser.setUser(currentUser);  // Устанавливаем связь
            imageUserRepository.save(imageUser);  // Сохраняем ImageUser
        }

        userRepository.save(currentUser);
    }

    @Override
    public void updatePassword(NewPassword newPassword) {
        UserDto userDtoAuth = getCurrentUser(); // Получаем текущего авторизованного пользователя

        // Загрузка текущего пользователя из базы данных
        User currentUser = currentUser(userDtoAuth);

        // Здесь добавляем логику для обновления пароля
        currentUser.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(currentUser);
    }

    @Override
    public User getUserFromUserDtoExtend(UserDto userDto) {
        return currentUser(userDto);
    }

    private User currentUser(UserDto userDtoAuth) {
        return userRepository.findById(userDtoAuth.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Получает идентификатор текущего авторизованного пользователя.
     *
     * @return идентификатор текущего пользователя.
     * @throws UsernameNotFoundException если пользователь не найден или не авторизован.
     */
    @Override
    public Long getCurrentUserId() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or not authenticated: " + username));
    }
}
