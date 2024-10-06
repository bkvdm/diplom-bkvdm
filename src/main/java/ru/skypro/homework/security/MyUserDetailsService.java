package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

/**
 * Сервис для загрузки пользовательских данных в системе безопасности.
 *
 * <p>
 * Этот класс реализует интерфейс {@link UserDetailsService} и предоставляет
 * метод для поиска пользователя по имени пользователя (в данном случае, по email).
 * Если пользователь не найден, выбрасывается {@link UsernameNotFoundException}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает данные пользователя по имени пользователя (email).
     *
     * <p>
     * Этот метод ищет пользователя в базе данных по заданному имени пользователя.
     * Если пользователь найден, возвращаются его данные в виде объекта {@link UserDetails}.
     * Если пользователь не найден, выбрасывается {@link UsernameNotFoundException}.
     * </p>
     *
     * @param username имя пользователя (email), по которому нужно найти пользователя.
     * @return объект {@link UserDetails}, содержащий информацию о пользователе.
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        user.orElseThrow(() -> new UsernameNotFoundException(username));
        return new MyUserPrincipal(user.get());
    }
}
