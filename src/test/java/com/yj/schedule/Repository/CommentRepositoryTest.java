package com.yj.schedule.Repository;

import com.yj.schedule.domain.comment.CommentRequestDto;
import com.yj.schedule.domain.schedule.ScheduleRequestDto;
import com.yj.schedule.domain.comment.Comment;
import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import com.yj.schedule.domain.user.UserRoleEnum;
import com.yj.schedule.domain.comment.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    private User user;
    private Schedule schedule;

    public void setup() {
        user = new User("test1234", "pwpw1234", UserRoleEnum.USER);
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setContents("test contents");
        requestDto.setTitle("test title");
        schedule = new Schedule(requestDto, user);
    }

    @Test
    public void testSaveComment() {
        this.setup();
        // given
        CommentRequestDto requestDto = new CommentRequestDto("test comment");
        Comment comment = new Comment(requestDto, schedule, user);
        comment.setCreator(user.getUsername());

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertTrue(savedComment.getId() > 0);
        assertEquals("test comment", savedComment.getComments());
    }

    @Test
    public void testFindAllComments() {
        this.setup();
        // given
        CommentRequestDto requestDto1 = new CommentRequestDto("test 1");
        CommentRequestDto requestDto2 = new CommentRequestDto("test 2");
        Comment comment1 = new Comment(requestDto1, schedule, user);
        Comment comment2 = new Comment(requestDto2, schedule, user);
        comment1.setCreator(user.getUsername());
        comment2.setCreator(user.getUsername());

        System.out.println(comment1.getComments());
        commentRepository.saveAll(List.of(comment1, comment2));

        // when
        List<Comment> comments = commentRepository.findAll();

        // then
        assertEquals(2, comments.size());
    }
}
