package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.ImageUser;

import java.util.Optional;

public interface ImageUserRepository extends JpaRepository<ImageUser, Long> {

    Optional<ImageUser> findByUserId(long id);
}
