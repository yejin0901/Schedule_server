package com.yj.schedule.domain.comment;


import com.yj.schedule.domain.schedule.ScheduleResponseDto;
import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import com.yj.schedule.domain.schedule.ScheduleRepository;
import com.yj.schedule.global.exception.IllegalAccessException;
import com.yj.schedule.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResponseDto createComment(CommentRequestDto requestDto, Long scheduleId, User user) {
        Schedule schedule = findSchedule(scheduleId);
        if(scheduleRepository.findById(scheduleId).isPresent()) {
            Comment comment = commentRepository.save(new Comment(requestDto, schedule, user));
            commentRepository.save(comment);
            return new ScheduleResponseDto(schedule);
        }
        else{
            throw new NotFoundException("해당일정이 없습니다.");
        }
    }

    @Transactional
    public ScheduleResponseDto updateComment(CommentRequestDto requestDto,Long scheduleId, Long commentId, User user) {
        Schedule schedule = findSchedule(scheduleId);
        Comment comment = findComment(commentId);
        if(!checkSelfUser(user, comment)){
            throw new IllegalAccessException("해당 사용자가 아닙니다.");
        }
        comment.setComments(requestDto.getComments());
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto deleteComment(Long scheduleId,Long commentId, User user) {
        Schedule schedule = findSchedule(scheduleId);
        Comment comment = findComment(commentId);
        if(!checkSelfUser(user, comment)){
            throw new IllegalAccessException("해당 사용자가 아닙니다.");
        }
        commentRepository.delete(comment);
        return new ScheduleResponseDto(schedule);
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 할일이 존재하지 않습니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 댓글이 존재하지 않습니다."));
    }

    private Boolean checkSelfUser(User user, Comment comment) {
        return user.getUsername().equals(comment.getCreator());
    }
}
