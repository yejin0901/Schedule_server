package com.yj.schedule.dto;

import com.yj.schedule.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String name;
    private String comments;

    public CommentResponseDto(Comment comment) {
        this.name = comment.getCreator();
        this.comments = comment.getComments();
    }
}
