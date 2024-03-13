package com.yj.schedule.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yj.schedule.global.config.WebSecurityConfig;
import com.yj.schedule.domain.user.UserController;
import com.yj.schedule.domain.login.SignupRequestDto;
import com.yj.schedule.global.jwt.JwtUtil;
import com.yj.schedule.domain.login.RefreshTokenService;
import com.yj.schedule.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    RefreshTokenService refreshTokenService;
    @MockBean
    JwtUtil jwtUtil;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    @Test
    public void testSignup_Successful() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("user1234");
        requestDto.setPassword("password123");

        given(userService.signup(requestDto)).willReturn(true);

        // When and Then
        mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}