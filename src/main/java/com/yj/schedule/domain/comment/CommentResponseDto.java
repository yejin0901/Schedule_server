package com.yj.schedule.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private String name;
    private String comments;

    public CommentResponseDto(Comment comment) {
        this.name = comment.getCreator();
        this.comments = comment.getComments();
    }
}
