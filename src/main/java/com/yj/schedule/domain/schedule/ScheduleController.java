package com.yj.schedule.domain.schedule;

import com.yj.schedule.global.CommonResponse;
import com.yj.schedule.global.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleServiceImpl scheduleServiceImpl;

    @PostMapping("/schedules")
    public ResponseEntity<CommonResponse<ScheduleResponseDto>> createSchedule(
            @RequestBody ScheduleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto response = scheduleServiceImpl.createSchedule(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<ScheduleResponseDto>builder()
                        .msg("일정이 생성되었습니다.")
                        .data(response)
                        .build());

    }

    @GetMapping("/all-schedules")
    public ResponseEntity<CommonResponse<List<ScheduleResponseDto>>> getAllSchedule() {
        Page<ScheduleResponseDto> response = scheduleServiceImpl.getAllSchedule();
        List<ScheduleResponseDto> scheduleList = response.getContent();
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<List<ScheduleResponseDto>>builder()
                        .msg("미완료된 일정이 전체조회되었습니다.")
                        .data(scheduleList)
                        .build());
    }

    @GetMapping("/pro-schedules")
    public ResponseEntity<CommonResponse<List<ScheduleResponseDto>>> getProSchedule() {
        List<ScheduleResponseDto> response = scheduleServiceImpl.getProSchedule();
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<List<ScheduleResponseDto>>builder()
                        .msg("선택 일정이 전체조회되었습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/schedules")
    public ResponseEntity<CommonResponse<List<ScheduleResponseDto>>> getUserSchedule(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<ScheduleResponseDto> response = scheduleServiceImpl.getUserSchedule(userDetails.getUser());
        List<ScheduleResponseDto> scheduleList = response.getContent();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<List<ScheduleResponseDto>>builder()
                        .msg("사용자 일정이 조회되었습니다.")
                        .data(scheduleList) // List 객체를 data로 설정
                        .build());
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<CommonResponse<ScheduleResponseDto>> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto response = scheduleServiceImpl.updateSchedule(id, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<ScheduleResponseDto>builder()
                        .msg("일정이 수정되었습니다.")
                        .data(response)
                        .build());
    }

}
