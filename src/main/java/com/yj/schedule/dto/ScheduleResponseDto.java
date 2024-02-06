package com.yj.schedule.dto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.yj.schedule.entity.Comment;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleResponseDto{
    private String title;
    private String contents;
    private String username;
    private String done;
    private String success;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public ScheduleResponseDto(Schedule schedule) {
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.username = schedule.getUser().getUsername();
        this.done = schedule.getDone();
        this.createdAt = schedule.getCreatedAt();
        for (Comment comment: schedule.getCommentList()){
            commentList.add(new CommentResponseDto(comment));
        }
        this.success = "success";
    }

    public ScheduleResponseDto(String message)
    {
        this.success = message;
    }

}
