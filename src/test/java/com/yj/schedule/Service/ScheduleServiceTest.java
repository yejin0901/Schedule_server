package com.yj.schedule.Service;

import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.entity.User;
import com.yj.schedule.repository.ScheduleRepository;
import com.yj.schedule.service.CommentService;
import com.yj.schedule.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;


    @Test
    public void testCreateSchedule() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setContents("content 1");
        requestDto.setTitle("title 1");

        Schedule schedule = new Schedule(requestDto, user);
        given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);

        // When
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, user);

        // Then
        assertNotNull(responseDto);
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getContents(), responseDto.getContents());
    }

    @Test
    public void testGetSchedule() {
        // Given
        Long scheduleId = 1L;
        User user = new User();
        user.setUsername("testUser");
        Schedule schedule = new Schedule();
        schedule.setUser(user);
        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));

        // When
        ScheduleResponseDto responseDto = scheduleService.getSchedule(scheduleId, user);

        // Then
        assertNotNull(responseDto);
    }

    @Test
    public void testUpdateSchedule() {
        // given
        Long scheduleId = 1L;
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Test Schedule");
        requestDto.setContents("Test Contents");
        requestDto.setDone("TRUE");

        User user = new User(); // 사용자 정보 설정
        user.setUsername("test1234");

        // Mock 데이터 생성
        Schedule existingSchedule = new Schedule();
        existingSchedule.setId(scheduleId);
        existingSchedule.setTitle("Existing Schedule");
        existingSchedule.setContents("Existing Contents");
        existingSchedule.setDone("FALSE");
        existingSchedule.setUser(user);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(existingSchedule));

        // when
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(scheduleId, requestDto, user);

        // then
        assertNotNull(responseDto);
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getContents(), responseDto.getContents());
        assertEquals(requestDto.getDone(), responseDto.getDone());
        assertEquals(existingSchedule.getCreatedAt(), responseDto.getCreatedAt());
    }

    @Test
    public void testGetAllSchedule() {
        // When
        ScheduleService scheduleService = new ScheduleService(scheduleRepository);
        scheduleService.getAllSchedule();
        // Then
        verify(scheduleRepository, times(1)).findAllByDoneEqualsOrderByCreatedAtDesc("FALSE");
    }
}
