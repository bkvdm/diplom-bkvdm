package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    /**
     * Получает роль пользователя по идентификатору пользователя не передавая экземпляр User в целях безопасности.
     */
    @Query("SELECT u.role FROM User u WHERE u.email = :email")
    Optional<Role> findRoleByEmail(@Param("email") String email);
}
