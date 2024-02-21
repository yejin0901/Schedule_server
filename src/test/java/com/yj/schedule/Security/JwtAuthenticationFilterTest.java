package com.yj.schedule.Security;

import com.yj.schedule.entity.User;
import com.yj.schedule.entity.UserRoleEnum;
import com.yj.schedule.jwt.JwtUtil;
import com.yj.schedule.security.JwtAuthenticationFilter;
import com.yj.schedule.security.UserDetailsImpl;
import com.yj.schedule.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void attemptAuthentication_ValidRequest() throws IOException {
        // given, mock설정
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String requestBody = "{\"username\":\"username\",\"password\":\"password\"}";
        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        given(request.getInputStream()).willReturn(new TestServletInputStream(inputStream));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("test1234", "pwpw1234");

        // 인증정보
        Authentication authentication = mock(Authentication.class);
        given(authenticationManager.authenticate(argThat(authenticationTokens ->
                "test1234".equals(authenticationToken.getPrincipal()) && "pwpw1234".equals(authenticationToken.getCredentials())))).willReturn(authentication);

        // when
        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        // then
        assertEquals(authentication, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void attemptAuthentication_InvalidRequest() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getInputStream()).thenThrow(IOException.class);

        // then
        assertThrows(RuntimeException.class, () -> jwtAuthenticationFilter.attemptAuthentication(request, response));
    }

    @Test
    void successfulAuthentication_ValidAuthentication() throws IOException, ServletException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUsername()).willReturn("username");

        User user = new User();
        user.setRole(UserRoleEnum.USER);
        user.setId(1L);

        given(userDetails.getUser()).willReturn(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(jwtUtil.createAccessToken("username", UserRoleEnum.USER)).thenReturn("access-token");
        when(jwtUtil.createRefreshToken("username")).thenReturn("refresh-token");


        // http body에 작성
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationFilter.successfulAuthentication(request, response, chain, authentication);

        verify(response).addHeader(eq(JwtUtil.AUTHORIZATION_HEADER), eq("access-token"));
        verify(refreshTokenService).saveToken(eq("refresh-token"), eq(user.getId()));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).println("로그인이 완료되었습니다.");
        verify(printWriter).flush(); // 응답 데이터 보장
    }


    // ServletInputStream provides a way to read this raw binary data from the request body.
    private static class TestServletInputStream extends ServletInputStream {
        private final InputStream inputStream;

        public TestServletInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }
    }

}
