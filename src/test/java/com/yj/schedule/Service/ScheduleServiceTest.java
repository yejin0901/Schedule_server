package com.yj.schedule.Service;

import com.yj.schedule.domain.schedule.ScheduleRequestDto;
import com.yj.schedule.domain.schedule.ScheduleResponseDto;
import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import com.yj.schedule.domain.schedule.ScheduleRepository;
import com.yj.schedule.domain.schedule.ScheduleServiceImpl;
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
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private ScheduleServiceImpl scheduleServiceImpl;

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
        ScheduleResponseDto responseDto = scheduleServiceImpl.createSchedule(requestDto, user);

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
        ScheduleResponseDto responseDto = scheduleServiceImpl.getSchedule(scheduleId, user);

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

        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(existingSchedule));

        // when
        ScheduleResponseDto responseDto = scheduleServiceImpl.updateSchedule(scheduleId, requestDto, user);

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
        scheduleServiceImpl.getAllSchedule();
        // Then
        verify(scheduleRepository, times(1)).findAllByDoneEqualsOrderByCreatedAtDesc("FALSE");
    }
}
