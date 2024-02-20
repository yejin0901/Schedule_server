package com.yj.schedule.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yj.schedule.config.WebSecurityConfig;
import com.yj.schedule.controller.CommentController;
import com.yj.schedule.controller.ScheduleController;
import com.yj.schedule.dto.CommentRequestDto;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.entity.User;
import com.yj.schedule.entity.UserRoleEnum;
import com.yj.schedule.security.UserDetailsImpl;
import com.yj.schedule.service.CommentService;
import com.yj.schedule.service.ScheduleService;
import org.junit.jupiter.api.BeforeAll;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = {ScheduleController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;
    private UsernamePasswordAuthenticationToken mockPrincipal;

    @MockBean
    ScheduleService scheduleService;

    @MockBean
    CommentService commentService;

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
    public void testCreateComment() throws Exception {

        this.mockUserSetup();
        CommentRequestDto requestDto = new CommentRequestDto("test comment");
        Long scheduleId = 1L;

        mockMvc.perform(post("/api/schedules/{scheduleId}/comments", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateComment() throws Exception {

        this.mockUserSetup();
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setComments("test comment");
        Long scheduleId = 1L;
        Long commentId = 1L;

        mockMvc.perform(put("/api/schedules/{scheduleId}/comments/{commentId}", scheduleId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteComment() throws Exception {

        this.mockUserSetup();
        Long scheduleId = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete("/api/schedules/{scheduleId}/comments/{commentId}", scheduleId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

}