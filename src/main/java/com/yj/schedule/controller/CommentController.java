package com.yj.schedule.controller;

import com.yj.schedule.dto.CommentRequestDto;
import com.yj.schedule.dto.CommonResponse;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.security.UserDetailsImpl;
import com.yj.schedule.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommonResponse<ScheduleResponseDto>> createComment(
            @PathVariable Long scheduleId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        ScheduleResponseDto response =  commentService.createComment(requestDto, scheduleId, request, userDetails.getUser());
            return ResponseEntity.ok()
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .statusCode(HttpStatus.OK.value())
                            .msg("댓글이 생성되었습니다.")
                            .data(response)
                            .build());
    }

    @PutMapping("/schedules/{scheduleId}/comments/{commentId}")
    public  ResponseEntity<CommonResponse<ScheduleResponseDto>> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        ScheduleResponseDto response = commentService.updateComment(requestDto, scheduleId, commentId, userDetails.getUser(), request);
        if(response.getSuccess().equals("fail-validate-user")){
            return ResponseEntity.badRequest()
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .msg("작성자만 수정/삭제할 수 있습니다.")
                            .data(response)
                            .build());
        }
        else {
            return ResponseEntity.ok()
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .statusCode(HttpStatus.OK.value())
                            .msg("댓글이 수정되었습니다.")
                            .data(response)
                            .build());
        }
    }

    @DeleteMapping("/schedules/{scheduleId}/comments/{commentId}")
    public  ResponseEntity<CommonResponse<ScheduleResponseDto>> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        ScheduleResponseDto response = commentService.deleteComment(scheduleId,commentId,userDetails.getUser(), request);
        if(response.getSuccess().equals("fail-validate-user")){
            return ResponseEntity.badRequest()
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .msg("작성자만 수정/삭제할 수 있습니다.")
                            .data(response)
                            .build());
        }
        else {
            return ResponseEntity.ok()
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .statusCode(HttpStatus.OK.value())
                            .msg("댓글이 삭제되었습니다.")
                            .data(response)
                            .build());
        }
    }
}
