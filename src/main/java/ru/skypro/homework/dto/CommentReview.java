package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentReview {

    private long count;
    private List<CommentResult> results;
}