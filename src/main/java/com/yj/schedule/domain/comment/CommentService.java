package com.yj.schedule.domain.comment;

import com.yj.schedule.domain.schedule.ScheduleResponseDto;
import com.yj.schedule.domain.user.User;

public interface CommentService {
    ScheduleResponseDto createComment(CommentRequestDto requestDto, Long scheduleId, User user);

    ScheduleResponseDto updateComment(CommentRequestDto requestDto, Long scheduleId, Long commentId, User user);

    ScheduleResponseDto deleteComment(Long scheduleId, Long commentId, User user);
}
