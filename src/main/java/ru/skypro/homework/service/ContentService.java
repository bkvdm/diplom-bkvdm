package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentReview;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.List;

public interface ContentService {

    List<CommentReview.CommentResult> getCommentsByAdId(long adId);

    CommentReview.CommentResult addComment(long adId, CreateOrUpdateComment commentData);

    CommentReview.CommentResult updateComment(long adId, long commentId, CreateOrUpdateComment commentData);

    void deleteComment(long adId, long commentId);
}
