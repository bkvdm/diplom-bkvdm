package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    /**
     * Получение списка всех объявлений по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя, для которого нужно получить объявления.
     * @return список объявлений, принадлежащих указанному пользователю.
     */
    List<Ad> findByUserId(Long userId);
}
