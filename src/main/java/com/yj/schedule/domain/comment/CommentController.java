package com.yj.schedule.domain.comment;

import com.yj.schedule.global.CommonResponse;
import com.yj.schedule.domain.schedule.ScheduleResponseDto;
import com.yj.schedule.global.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;

    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommonResponse<ScheduleResponseDto>> createComment(
            @PathVariable Long scheduleId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        ScheduleResponseDto response =  commentServiceImpl.createComment(requestDto, scheduleId, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .msg("댓글이 생성되었습니다.")
                            .data(response)
                            .build());
    }

    @PutMapping("/schedules/{scheduleId}/comments/{commentId}")
    public  ResponseEntity<CommonResponse<ScheduleResponseDto>> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
            ScheduleResponseDto response = commentServiceImpl.updateComment(requestDto, scheduleId, commentId, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .msg("댓글이 수정되었습니다.")
                            .data(response)
                            .build());
    }

    @DeleteMapping("/schedules/{scheduleId}/comments/{commentId}")
    public  ResponseEntity<CommonResponse<ScheduleResponseDto>> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
            ScheduleResponseDto response = commentServiceImpl.deleteComment(scheduleId,commentId,userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .msg("작성자만 수정/삭제할 수 있습니다.")
                            .data(response)
                            .build());
    }
}
