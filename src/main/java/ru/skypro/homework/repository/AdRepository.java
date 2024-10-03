package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {

    /**
     * Находит последнее объявление (Ad) пользователя по его идентификатору.
     * <p>
     * Запрос для получения последнего объявления на основе максимального значения
     * идентификатора объявления (id) для указанного пользователя (userId). Объявления сортируются
     * по убыванию значения id, в результате в списке оказывается первое объявление (с наибольшим id).
     *
     * @param userId идентификатор пользователя, для которого нужно найти последнее объявление
     * @return Optional, содержащий объявление, если оно существует, иначе пустое значение
     */
    Optional<Ad> findTopByUserIdOrderByIdDesc(Long userId);

    /**
     * Получение списка всех объявлений по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя, для которого нужно получить объявления.
     * @return список объявлений, принадлежащих указанному пользователю.
     */
    List<Ad> findByUserId(Long userId);
}
