package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.CommentReviewMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ContentService;
import ru.skypro.homework.service.UserService;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {

    private final CommentRepository commentRepository;
    private final CommentReviewMapper commentReviewMapper;
    private final AdRepository adRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ContentServiceImpl(CommentRepository commentRepository, CommentReviewMapper commentReviewMapper, AdRepository adRepository, UserService userService, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentReviewMapper = commentReviewMapper;
        this.adRepository = adRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Получает список комментариев для объявления по его идентификатору.
     *
     * <p>Метод возвращает список комментариев, связанных с конкретным объявлением.</p>
     *
     * @param adId идентификатор объявления, для которого требуется получить комментарии
     * @return объект {@link CommentReview}, содержащий список комментариев в виде {@link CommentReview}
     * @throws NoSuchElementException если объявление с указанным id не найдено
     */
    @Override
    public CommentReview getCommentsByAdId(long adId) {
        List<Comment> comments = commentRepository.findByAdId(adId);

        return commentReviewMapper.toCommentReview(comments);
    }

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * <p>Этот метод позволяет авторизованным пользователям оставлять комментарии к существующим объявлениям.</p>
     * <p>Комментарий создается от имени текущего пользователя, и время создания фиксируется автоматически.</p>
     *
     * @param adId        идентификатор объявления, к которому добавляется комментарий
     * @param commentData DTO с данными нового комментария (текст)
     * @return созданный комментарий, представлен в виде объекта {@link CommentResult}
     * @throws NoSuchElementException если объявление или пользователь не найдены
     */
    @Override
    public CommentResult addComment(long adId, CreateOrUpdateComment commentData) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NoSuchElementException("Объявление с id " + adId + " не найдено"));

        UserDto userDto = userService.getCurrentUser();

        long currentUserId = userService.getCurrentUserId();

        Optional<User> userOptional = userRepository.findById(currentUserId);

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("The user not found with id: " + currentUserId);
        }

        Comment comment = new Comment();
        comment.setText(commentData.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(userOptional.get());
        comment.setAd(ad);

        comment = commentRepository.save(comment);

        return commentReviewMapper.toCommentResult(comment);
    }

    /**
     * Обновляет существующий комментарий.
     * <p>
     * Только авторизованные пользователи могут обновлять комментарии. Пользователи с ролью {@code USER} могут обновлять только свои комментарии,
     * то есть те, где они являются авторами. Пользователи с ролью {@code ADMIN} могут обновлять любые комментарии, независимо от того, кто их создал.
     * Аннотация {@code @PreAuthorize("hasRole('ADMIN') or #comment.user.id == principal.id")} над эндпоинтом, это обеспечивает.
     *
     * @param adId        идентификатор объявления, к которому относится комментарий
     * @param commentId   идентификатор комментария, который нужно обновить
     * @param commentData данные для обновления комментария в формате {@link CreateOrUpdateComment}
     * @return объект {@link CommentResult}, представляющий обновленный комментарий
     * @throws NoSuchElementException если объявление или комментарий не найдены
     * @throws AccessDeniedException  если текущий пользователь не имеет прав на обновление данного комментария
     */
    @Override
    public CommentResult updateComment(long adId, long commentId, CreateOrUpdateComment commentData) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NoSuchElementException("Объявление с id " + adId + " не найдено"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Комментарий с id " + commentId + " не найден"));

        UserDto currentUser = userService.getCurrentUser();

        if (currentUser.getRole() == Role.USER && comment.getUser().getId() != currentUser.getId()) {
            throw new AccessDeniedException("У вас нет прав на обновление этого комментария.");
        }

        // Обновление только текста комментария
        comment.setText(commentData.getText());
        comment = commentRepository.save(comment);

        return commentReviewMapper.toCommentResult(comment);
    }

    /**
     * Удаляет комментарий по идентификатору.
     *
     * <p>Пользователи с ролью {@code ADMIN} могут удалять любые комментарии.
     * Пользователи с ролью {@code USER} могут удалять только свои комментарии,
     * которые они создали.</p>
     *
     * <p>Если текущий пользователь с ролью {@code USER} пытается удалить чужой комментарий,
     * выбрасывается {@link SecurityException}.</p>
     *
     * @param adId      идентификатор объявления, к которому относится комментарий
     * @param commentId идентификатор комментария, который нужно удалить
     * @throws NoSuchElementException если комментарий или объявление не найдено
     * @throws SecurityException      если пользователь не имеет прав на удаление данного комментария
     */
    @Override
    public void deleteComment(long adId, long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Комментарий с id " + commentId + " не найден"));

        long currentUserId = userService.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id " + currentUserId + " не найден"));

        if (currentUser.getRole() == Role.USER && comment.getUser().getId() != currentUserId) {
            throw new SecurityException("Пользователь не имеет прав на удаление данного комментария");
        }

        commentRepository.delete(comment);
    }
}
