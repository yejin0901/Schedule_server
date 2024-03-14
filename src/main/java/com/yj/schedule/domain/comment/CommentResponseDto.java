package com.yj.schedule.domain.comment;

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
