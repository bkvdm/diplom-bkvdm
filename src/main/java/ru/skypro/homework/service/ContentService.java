package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentResult;
import ru.skypro.homework.dto.CommentReview;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface ContentService {

    CommentReview getCommentsByAdId(long adId);

    CommentResult addComment(long adId, CreateOrUpdateComment commentData);

    CommentResult updateComment(long adId, long commentId, CreateOrUpdateComment commentData);

    void deleteComment(long adId, long commentId);
}
