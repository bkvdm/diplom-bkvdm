package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.model.ImageUser;

import java.util.Optional;

public interface ImageAdRepository extends JpaRepository<ImageAd, Long> {

    Optional<ImageAd> findByAdId(long id);
}
