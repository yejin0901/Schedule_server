package com.yj.schedule.domain.schedule;

import com.yj.schedule.global.CommonResponse;
import com.yj.schedule.global.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/schedules/{id}")
    public ResponseEntity<CommonResponse<ScheduleResponseDto>> getSelectSchedule(
            @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ScheduleResponseDto response = scheduleServiceImpl.getSchedule(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<ScheduleResponseDto>builder()
                        .msg("일정이 조회되었습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/all-schedules")
    public ResponseEntity<CommonResponse<List<ScheduleResponseDto>>> getAllSchedule() {
        List<ScheduleResponseDto> response = scheduleServiceImpl.getAllSchedule();
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<List<ScheduleResponseDto>>builder()
                        .msg("전체일정이 조회되었습니다.")
                        .data(response)
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
