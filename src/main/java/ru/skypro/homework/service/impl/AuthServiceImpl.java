package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param userDetailsService сервис для загрузки пользователей
     * @param passwordEncoder    компонент для шифрования паролей
     * @param userRepository     репозиторий для работы с пользователями
     * @param userMapper         маппер для преобразования DTO в сущности и обратно
     */
    public AuthServiceImpl(UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           UserMapper userMapper) {
        this.userDetailsService = userDetailsService;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param userName логин (имя пользователя)
     * @param password пароль пользователя
     * @return true, если аутентификация успешна, иначе false
     */
    @Override
    public boolean login(String userName, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Выполняет регистрацию нового пользователя.
     *
     * @param register DTO с данными регистрации
     * @return true, если регистрация успешна, иначе false
     */
    @Override
    public boolean register(Register register) {
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            return false;
        }

        User user = userMapper.toEntity(register);
        user.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(user);
        return true;
    }

    /**
     * Возвращает email текущего авторизованного пользователя.
     *
     * @return Optional, содержащий email текущего пользователя, если он аутентифицирован,
     * или пустой Optional, если пользователь не найден.
     */
    @Override
    public Optional<String> getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        } else {
            return Optional.empty();
        }
    }
}
