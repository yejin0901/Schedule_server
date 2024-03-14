package com.yj.schedule.Entity;

import com.yj.schedule.domain.comment.CommentRequestDto;
import com.yj.schedule.domain.schedule.ScheduleRequestDto;
import com.yj.schedule.domain.comment.Comment;
import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleCommentEntityTest {
    @Test
    public void testScheduleEntityConstructor() {
        // given
        String title = "Test Title";
        String contents = "Test Contents";
        User user = new User();
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle(title);
        requestDto.setContents(contents);

        //when
        Schedule schedule = new Schedule(requestDto, user);

        //then
        assertEquals(title, schedule.getTitle());
        assertEquals(contents, schedule.getContents());
        assertEquals("FALSE", schedule.getDone());
        assertEquals(user, schedule.getUser());
    }

    @Test
    public void testScheduleEntityCommentList() {
        // given
        Schedule schedule = new Schedule();
        User user = new User();

        CommentRequestDto requestDto1 = new CommentRequestDto();
        CommentRequestDto requestDto2 = new CommentRequestDto();
        requestDto1.setComments("comment 1");
        requestDto2.setComments("comment 2");

        Comment comment1 = new Comment(requestDto1, schedule, user);
        Comment comment2 = new Comment(requestDto2, schedule, user);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        // when
        schedule.setCommentList(comments);

        // then
        assertEquals(comments, schedule.getCommentList());
    }

}