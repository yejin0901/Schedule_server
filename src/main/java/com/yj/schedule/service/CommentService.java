package com.yj.schedule.service;


import com.yj.schedule.dto.CommentRequestDto;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.entity.Comment;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.entity.User;
import com.yj.schedule.jwt.JwtUtil;
import com.yj.schedule.repository.CommentRepository;
import com.yj.schedule.repository.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final JwtUtil jwtUtil;

    public ScheduleResponseDto createComment(CommentRequestDto requestDto, Long scheduleId, HttpServletRequest request, User user) {
        String token = jwtUtil.getJwtFromHeader(request);
        jwtUtil.validateToken(token);
        Schedule schedule = findSchedule(scheduleId);
        if(scheduleRepository.findById(scheduleId).isPresent()) {
            Comment comment = commentRepository.save(new Comment(requestDto, schedule, user));
            commentRepository.save(comment);
            return new ScheduleResponseDto(schedule);
        }
        else{
            throw new IllegalArgumentException("해당일정이 없습니다.");
        }
    }

    @Transactional
    public ScheduleResponseDto updateComment(CommentRequestDto requestDto,Long scheduleId, Long commentId, User user, HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);
        jwtUtil.validateToken(token);
        Schedule schedule = findSchedule(scheduleId);
        Comment comment = findComment(commentId);
        if(!checkSelfUser(user, comment)){
            return new ScheduleResponseDto(("fail-validate-user"));
        }
        comment.setComments(requestDto.getComments());
        commentRepository.save(comment);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto deleteComment(Long scheduleId,Long commentId, User user, HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);
        jwtUtil.validateToken(token);
        Schedule schedule = findSchedule(scheduleId);
        Comment comment = findComment(commentId);
        if(!checkSelfUser(user, comment)){
            return new ScheduleResponseDto(("fail-validate-user"));
        }
        commentRepository.delete(comment);
        return new ScheduleResponseDto(schedule);
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 할일이 존재하지 않습니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
    }

    private Boolean checkSelfUser(User user, Comment comment) {
        return user.getUsername().equals(comment.getCreator());
    }
}
