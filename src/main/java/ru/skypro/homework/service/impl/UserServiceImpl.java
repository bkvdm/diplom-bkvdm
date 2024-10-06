package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
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

    /**
     * Получает текущего авторизованного пользователя.
     *
     * <p>
     * Этот метод извлекает имя пользователя из контекста безопасности и использует его для поиска
     * соответствующего пользователя в базе данных. Если пользователь не найден или не авторизован,
     * выбрасывается исключение {@link UsernameNotFoundException}.
     * </p>
     *
     * @return объект {@link UserDto}, представляющий текущего авторизованного пользователя.
     * @throws UsernameNotFoundException если пользователь не найден или не авторизован.
     */
    @Override
    public UserDto getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or not authenticated: " + username));
    }

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * <p>
     * Метод обновляет имя, фамилию и номер телефона текущего авторизованного пользователя.
     * Если пользователь имеет связанный объект {@link ImageUser}, то он также сохраняет изменения
     * для этого объекта. После обновления данные пользователя сохраняются в базе данных.
     * </p>
     *
     * @param userDto объект {@link UserDto}, содержащий обновленную информацию о пользователе.
     */
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

    /**
     * Обновляет пароль текущего авторизованного пользователя.
     *
     * <p>
     * Этот метод получает текущего авторизованного пользователя, затем обновляет его пароль,
     * используя новый пароль из объекта {@link NewPassword}. Пароль шифруется перед сохранением
     * в базе данных.
     * </p>
     *
     * @param newPassword объект {@link NewPassword}, содержащий новый пароль пользователя.
     */
    @Override
    public void updatePassword(NewPassword newPassword) {
        UserDto userDtoAuth = getCurrentUser(); // Получаем текущего авторизованного пользователя

        // Загрузка текущего пользователя из базы данных
        User currentUser = currentUser(userDtoAuth);

        // Здесь добавляем логику для обновления пароля
        currentUser.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(currentUser);
    }

    /**
     * Получает объект {@link User} из {@link UserDto}.
     *
     * <p>
     * Этот метод использует {@link UserDto} для поиска текущего пользователя в базе данных.
     * </p>
     *
     * @param userDto объект {@link UserDto}, содержащий информацию о пользователе.
     * @return объект {@link User}, соответствующий указанному {@link UserDto}.
     */
    @Override
    public User getUserFromUserDtoExtend(UserDto userDto) {
        return currentUser(userDto);
    }

    /**
     * Загружает текущего пользователя из базы данных на основе его {@link UserDto}.
     *
     * <p>
     * Метод использует {@link UserDto} для поиска пользователя по идентификатору.
     * Если пользователь не найден, выбрасывается исключение {@link IllegalArgumentException}.
     * </p>
     *
     * @param userDtoAuth объект {@link UserDto}, содержащий информацию о текущем пользователе.
     * @return объект {@link User}, соответствующий указанному {@link UserDto}.
     * @throws IllegalArgumentException если пользователь не найден.
     */
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

    /**
     * Проверяет, является ли текущий пользователь администратором.
     *
     * @return true, если текущий пользователь имеет роль ADMIN, иначе false.
     */
    @Override
    public boolean isCurrentUserAdmin() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findRoleByEmail(username)
                .map(role -> role.equals(Role.ADMIN))
                .orElseThrow(() -> new UsernameNotFoundException("User not found or not authenticated: " + username));
    }
}
