package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Поиск комментариев по идентификатору объявления.
     *
     * @param adId идентификатор объявления
     * @return список комментариев, связанных с объявлением
     */
    List<Comment> findByAdId(long adId);
}
