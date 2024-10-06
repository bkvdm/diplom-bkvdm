package ru.skypro.homework.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Реализация интерфейса {@link UserDetails} для представления пользователя в системе безопасности.
 *
 * <p>
 * Этот класс инкапсулирует информацию о пользователе, включая его роль и учётные данные,
 * и предоставляет методы для доступа к данным пользователя, требуемые для аутентификации и авторизации.
 * </p>
 */
public class MyUserPrincipal implements UserDetails {
    private final User user;

    public MyUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
