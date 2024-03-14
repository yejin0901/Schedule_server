package com.yj.schedule.domain.schedule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.yj.schedule.domain.comment.CommentResponseDto;
import com.yj.schedule.domain.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleResponseDto{
    private String id;
    private String title;
    private String contents;
    private String username;
    private String done;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public ScheduleResponseDto(Schedule schedule) {
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.username = schedule.getUser().getUsername();
        this.done = schedule.getDone();
        this.createdAt = schedule.getCreatedAt();
        for (Comment comment : schedule.getCommentList()){
            commentList.add(new CommentResponseDto(comment));
        }
    }
}
