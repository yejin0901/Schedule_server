package com.yj.schedule.DTO;

import com.yj.schedule.domain.comment.CommentRequestDto;
import com.yj.schedule.domain.schedule.ScheduleRequestDto;
import com.yj.schedule.domain.schedule.ScheduleResponseDto;
import com.yj.schedule.domain.comment.Comment;
import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleCommentDtoTest {

    @Test
    public void testScheduleDto() {
        // given
        User user = new User();
        user.setUsername("Test user");

        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setTitle("Test title");
        scheduleRequestDto.setContents("Test contents");
        Schedule schedule = new Schedule(scheduleRequestDto, user);

        CommentRequestDto requestDto = new CommentRequestDto("test comment");
        Comment comment = new Comment();
        comment.setComments(requestDto.getComments());
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        schedule.setCommentList(commentList);

        // when
        ScheduleResponseDto dto = new ScheduleResponseDto(schedule);

        // then
        assertEquals("Test title", dto.getTitle());
        assertEquals("Test contents", dto.getContents());
        assertEquals("FALSE", dto.getDone());
        assertEquals(1, dto.getCommentList().size());
    }

}