package com.yj.schedule.Repository;


import com.yj.schedule.domain.schedule.ScheduleRequestDto;
import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import com.yj.schedule.domain.user.UserRoleEnum;
import com.yj.schedule.domain.schedule.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @BeforeEach
    public void setUp() {
        // 테스트용 스케줄 데이터 추가
        User user = new User("test1234", "pwpw1234", UserRoleEnum.USER);

        ScheduleRequestDto requestDto1 = new ScheduleRequestDto();
        requestDto1.setContents("content 1");
        requestDto1.setTitle("title 1");

        ScheduleRequestDto requestDto2 = new ScheduleRequestDto();
        requestDto2.setContents("content 2");
        requestDto2.setTitle("title 2");

        Schedule schedule1 = new Schedule(requestDto1, user);
        Schedule schedule2 = new Schedule(requestDto2, user);

        schedule1.setDone("TRUE");//TRUE로 변경

        schedule1.setUser(user);
        schedule2.setUser(user);

        scheduleRepository.saveAll(List.of(schedule1, schedule2));
    }

    @Test
    public void testFindAllByDoneEqualsOrderByCreatedAtDesc() {
        // Given
        String doneStatus = "FALSE";

        // When
        List<Schedule> schedules = scheduleRepository.findAllByDoneEqualsOrderByCreatedAtDesc(doneStatus);

        // Then
        assertEquals(1, schedules.size());
        assertEquals("title 2", schedules.get(0).getTitle());
    }

}
