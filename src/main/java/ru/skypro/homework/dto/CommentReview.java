package ru.skypro.homework.dto;

import java.util.List;
import lombok.Data;

@Data
public class CommentReview {

    private int count;
    private List<CommentResult> results;

}
