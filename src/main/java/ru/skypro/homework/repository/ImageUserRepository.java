package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.ImageUser;

import java.util.Optional;

public interface ImageUserRepository extends JpaRepository<ImageUser, Long> {

    /**
     * Находит изображение пользователя по его идентификатору.
     *
     * <p>
     * Этот метод ищет запись изображения пользователя в базе данных
     * по указанному идентификатору пользователя. Если запись найдена,
     * возвращается объект {@link Optional} с изображением пользователя.
     * В противном случае возвращается пустой объект {@link Optional}.
     * </p>
     *
     * @param id идентификатор пользователя, для которого необходимо найти изображение.
     * @return {@link Optional} содержащий найденное изображение пользователя,
     * или пустой {@link Optional}, если изображение не найдено.
     */
    Optional<ImageUser> findByUserId(long id);
}
