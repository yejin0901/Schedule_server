package com.yj.schedule.Service;

import com.yj.schedule.domain.comment.CommentRequestDto;
import com.yj.schedule.domain.schedule.ScheduleRequestDto;
import com.yj.schedule.domain.schedule.ScheduleResponseDto;
import com.yj.schedule.domain.comment.Comment;
import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import com.yj.schedule.domain.user.UserRoleEnum;
import com.yj.schedule.domain.comment.CommentRepository;
import com.yj.schedule.domain.schedule.ScheduleRepository;
import com.yj.schedule.domain.comment.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private CommentServiceImpl commentServiceImpl;
    private Schedule testSchedule;
    private User user;

    @BeforeEach
    public void mockSetup() {
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("title 1");
        requestDto.setContents("content 1");
        user = new User("test", "1234", UserRoleEnum.USER);
        user.setUsername("test");
        testSchedule = new Schedule(requestDto, user);
    }

    @Test
    public void testCreateComment() {
        // Given
        Long scheduleId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("Test comment");
        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(testSchedule));

        // When
        ScheduleResponseDto responseDto = commentServiceImpl.createComment(requestDto, scheduleId, user);

        // Then
        assertNotNull(responseDto);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testUpdateComment() {
        // Given
        Long scheduleId = 1L;
        Long commentId = 1L;
        user.setUsername("test");

        Comment existComment = new Comment(new CommentRequestDto("origin comment 1"), testSchedule, user);
        CommentRequestDto requestDto = new CommentRequestDto("Updated comment");

        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(testSchedule));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(existComment));

        // When
        ScheduleResponseDto responseDto = commentServiceImpl.updateComment(requestDto, scheduleId, commentId, user);

        // Then
        assertNotNull(responseDto);
        assertEquals(requestDto.getComments(), existComment.getComments());
        verify(commentRepository, times(1)).save(existComment);
    }

    @Test
    public void testDeleteComment() {
        // Given
        Long scheduleId = 1L;
        Long commentId = 1L;
        user.setUsername("test");
        Schedule schedule = new Schedule();
        Comment comment = new Comment(new CommentRequestDto("Test comment"), schedule, user);
        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(testSchedule));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // When
        ScheduleResponseDto responseDto = commentServiceImpl.deleteComment(scheduleId, commentId, user);

        // Then
        assertNotNull(responseDto);
        verify(commentRepository, times(1)).delete(comment);
    }
}
