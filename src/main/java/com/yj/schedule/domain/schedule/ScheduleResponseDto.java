package com.yj.schedule.domain.schedule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.yj.schedule.domain.comment.CommentResponseDto;
import com.yj.schedule.domain.comment.Comment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDto{
    private String title;
    private String contents;
    private String done;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public ScheduleResponseDto(Schedule schedule) {
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.done = schedule.getDone();
        this.createdAt = schedule.getCreatedAt();
        for (Comment comment : schedule.getCommentList()){
            commentList.add(new CommentResponseDto(comment));
        }
    }

    public ScheduleResponseDto(ScheduleProjection scheduleProjection) {
        this.title = scheduleProjection.getTitle();
        this.contents = scheduleProjection.getContents();
        this.done = scheduleProjection.getDone();
        this.createdAt = scheduleProjection.getCreatedAt();
        this.commentList = scheduleProjection.getComments();
    }

    public ScheduleResponseDto(ScheduleProjection projection, List<CommentResponseDto> commentDtos) {
        this.title = projection.getTitle();
        this.contents = projection.getContents();
        commentList = commentDtos;
    }
}
