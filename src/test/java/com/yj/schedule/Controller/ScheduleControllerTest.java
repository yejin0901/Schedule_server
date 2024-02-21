package com.yj.schedule.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yj.schedule.config.WebSecurityConfig;
import com.yj.schedule.controller.ScheduleController;
import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.entity.User;
import com.yj.schedule.entity.UserRoleEnum;
import com.yj.schedule.security.UserDetailsImpl;
import com.yj.schedule.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = {ScheduleController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class ScheduleControllerTest {

    @MockBean
    ScheduleService scheduleService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    private UsernamePasswordAuthenticationToken mockPrincipal;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        User testUser = new User("test1234", "pwpw1234", UserRoleEnum.USER);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    public void testCreateSchedule() throws Exception {
        this.mockUserSetup();
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Test Title");
        requestDto.setContents("Test Contents");

        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetSelectSchedule() throws Exception {
        this.mockUserSetup();
        Long scheduleId = 1L;

        mockMvc.perform(get("/api/schedules/{id}", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void testGetAllSchedule() throws Exception {
        this.mockUserSetup();

        mockMvc.perform(get("/api/all-schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateSchedule() throws Exception {
        this.mockUserSetup();
        Long scheduleId = 1L;

        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("update Title");
        requestDto.setContents("update Contents");

        mockMvc.perform(put("/api/schedules/{id}", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }


}

