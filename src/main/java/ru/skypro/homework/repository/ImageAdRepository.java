package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.ImageAd;

import java.util.Optional;

public interface ImageAdRepository extends JpaRepository<ImageAd, Long> {

    /**
     * Находит изображение объявления по его идентификатору.
     *
     * <p>
     * Этот метод ищет запись изображения объявления в базе данных
     * по указанному идентификатору объявления. Если запись найдена,
     * возвращается объект {@link Optional} с изображением объявления.
     * Иначе возвращается пустой объект {@link Optional}.
     * </p>
     *
     * @param id идентификатор объявления, для которого необходимо найти изображение.
     * @return {@link Optional} содержащий найденное изображение объявления,
     * или пустой {@link Optional}, если изображение не найдено.
     */
    Optional<ImageAd> findByAdId(long id);
}
