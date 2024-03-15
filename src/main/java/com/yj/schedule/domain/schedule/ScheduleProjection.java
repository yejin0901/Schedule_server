package com.yj.schedule.domain.schedule;

import com.yj.schedule.domain.comment.CommentResponseDto;
import com.yj.schedule.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ScheduleProjection {
    private Long id;
    private String title;
    private String contents;
    private String done;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;
}
