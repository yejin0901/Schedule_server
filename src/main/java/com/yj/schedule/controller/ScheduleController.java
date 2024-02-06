package com.yj.schedule.controller;


import com.yj.schedule.dto.CommonResponse;
import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.security.UserDetailsImpl;
import com.yj.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<CommonResponse<ScheduleResponseDto>> createSchedule(
            @RequestBody ScheduleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request) {
        ScheduleResponseDto response = scheduleService.createSchedule(requestDto, userDetails.getUser(), request);
        return ResponseEntity.ok()
                .body(CommonResponse.<ScheduleResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("일정이 생성되었습니다.")
                        .data(response)
                        .build());

    }

    @GetMapping("/schedules/{id}")
    public  ResponseEntity<CommonResponse<ScheduleResponseDto>> getSelectSchedule(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl userDetails){
        ScheduleResponseDto response = scheduleService.getSchedule(id, userDetails.getUser());
        log.info(response.getSuccess());
        if(response.getSuccess().equals("fail-validate-user")){
            log.info("controller");
            return ResponseEntity.badRequest()
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .msg("작성자만 조회할 수 있습니다.")
                            .build());
        }
        else {
            return ResponseEntity.ok()
                    .body(CommonResponse.<ScheduleResponseDto>builder()
                            .statusCode(HttpStatus.OK.value())
                            .msg("일정이 조회되었습니다.")
                            .data(response)
                            .build());
        }

    }

    @GetMapping("/all-schedules")
    public  ResponseEntity<CommonResponse<List<ScheduleResponseDto>>> getAllSchedule(){
        List<ScheduleResponseDto> response = scheduleService.getAllSchedule();
        return ResponseEntity.ok()
                .body(CommonResponse.<List<ScheduleResponseDto>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("전체일정이 조회되었습니다.")
                        .data(response)
                        .build());
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<CommonResponse<ScheduleResponseDto>> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request){
        ScheduleResponseDto response = scheduleService.updateSchedule(id, requestDto,userDetails.getUser(), request);
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
                            .msg("일정이 수정되었습니다.")
                            .data(response)
                            .build());
        }
    }

}
