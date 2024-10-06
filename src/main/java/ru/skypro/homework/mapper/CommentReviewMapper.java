package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CommentResult;
import ru.skypro.homework.dto.CommentReview;
import ru.skypro.homework.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentReviewMapper {

    CommentReviewMapper INSTANCE = Mappers.getMapper(CommentReviewMapper.class);

    // Для преобразования Comment -> CommentResult
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.id", target = "author")
    CommentResult toCommentResult(Comment comment);

    // Для преобразования списка Comment -> CommentReview
    default CommentReview toCommentReview(List<Comment> comments) {
        List<CommentResult> commentResults = comments.stream()
                .map(this::toCommentResult)
                .collect(Collectors.toList());
        CommentReview commentReview = new CommentReview();
        commentReview.setCount(commentResults.size());
        commentReview.setResults(commentResults);
        return commentReview;
    }
}
